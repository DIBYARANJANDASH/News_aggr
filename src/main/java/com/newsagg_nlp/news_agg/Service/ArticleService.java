package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.ArticleEntity;
import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import com.newsagg_nlp.news_agg.Entity.UserPreferencesEntity;
import com.newsagg_nlp.news_agg.Repo.ArticleRepo;
import com.newsagg_nlp.news_agg.Repo.SubCategoryRepo;
import com.newsagg_nlp.news_agg.Repo.UserPreferenceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepo articleRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final UserPreferenceRepo userPreferenceRepo;
    private final RestTemplate restTemplate;

    private static final Duration CACHE_EXPIRY = Duration.ofHours(6);

    @Autowired
    public ArticleService(ArticleRepo articleRepo, SubCategoryRepo subCategoryRepo, RestTemplate restTemplate,
                          UserPreferenceRepo userPreferenceRepo) {
        this.articleRepo = articleRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.restTemplate = restTemplate;
        this.userPreferenceRepo = userPreferenceRepo;
    }

    /**
     * Fetch articles from an API and store them in Redis and DB.
     */
    @Cacheable(value = "articles", key = "#subcategory")
    public List<ArticleEntity> fetchArticlesFromApi(String category, String subcategory) {
        logger.info("Fetching articles for subcategory: {}", subcategory);

        // Fetch subcategory ID
        Optional<SubCategoryEntity> subCategoryEntityOpt = Optional.ofNullable(subCategoryRepo.findBySubcategoryName(subcategory));
        if (subCategoryEntityOpt.isEmpty()) {
            logger.warn("Subcategory not found: {}", subcategory);
            return List.of(); // Return empty list if subcategory is not found
        }

        String subcategoryId = subCategoryEntityOpt.get().getSubcategoryId();

        // Check if articles are outdated
        boolean isDataOutdated = articleRepo.findBySubcategoryId(subcategoryId).stream()
                .map(ArticleEntity::getPublishedAt)
                .max(LocalDateTime::compareTo)
                .map(latestDate -> Duration.between(latestDate, LocalDateTime.now()).compareTo(Duration.ofDays(2)) > 0)
                .orElse(true);

        if (!isDataOutdated) {
            return articleRepo.findBySubcategoryId(subcategoryId);
        }

        // Fetch new articles from API
        String apiUrl = "https://gnews.io/api/v4/search?q=" + subcategory + "&apikey=f0340de4da4a90909cc6787aefbc2691&lang=en";
        ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(apiUrl, NewsApiResponse.class);

        NewsApiResponse newsApiResponse = response.getBody();
        if (newsApiResponse != null && newsApiResponse.getArticles() != null) {
            List<ArticleEntity> newArticles = newsApiResponse.getArticles();

            // Save articles to database
            articleRepo.saveAll(newArticles);

            // Clear cache for this subcategory
            clearCache(subcategory);

            return newArticles;
        }

        logger.warn("No articles fetched from API for subcategory: {}", subcategory);
        return List.of();
    }

    private List<ArticleEntity> filterDuplicateArticles(List<ArticleEntity> newArticles, String subcategoryId) {
        List<ArticleEntity> existingArticles = articleRepo.findBySubcategoryId(subcategoryId);
        return newArticles.stream()
                .filter(newArticle -> existingArticles.stream()
                        .noneMatch(existing -> existing.getTitle().equals(newArticle.getTitle())))
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) //
    public void updateNewsForAllSubcategories() {
        logger.info("Scheduled task: Fetching fresh news for all subcategories...");

        List<SubCategoryEntity> subcategories = subCategoryRepo.findAll();
        subcategories.forEach(subCategory -> {
            fetchArticlesFromApi(subCategory.getCategory().getCategoryName(), subCategory.getSubcategoryName());
        });

        logger.info("Scheduled task completed.");
    }

    public void updateNewsForSubcategory(String subcategoryId) {
        SubCategoryEntity subCategory = subCategoryRepo.findById(subcategoryId)
                .orElseThrow(() -> new RuntimeException("Subcategory not found"));

        fetchArticlesFromApi(subCategory.getCategory().getCategoryName(), subCategory.getSubcategoryName());
    }

    @Cacheable(value = "articlesBySubcategory", key = "#subcategoryId")
    public List<ArticleEntity> getArticlesBySubcategory(String subcategoryId) {
        logger.info("Fetching articles for subcategory ID: {}", subcategoryId);
        return articleRepo.findBySubcategoryId(subcategoryId);
    }

    @Cacheable(value = "articlesByPreferences", key = "#userId")
    public List<ArticleEntity> getArticlesByUserPreferences(String userId) {
        logger.info("Fetching articles for user ID: {}", userId);

        // Fetch user preferences ordered by priority
        List<UserPreferencesEntity> userPreferences = userPreferenceRepo.findByUser_userIdOrderByPriorityAsc(userId); // Using ordered repository method
        if (userPreferences.isEmpty()) {
            logger.warn("No preferences found for user ID: {}", userId);
            return List.of();
        }

        // Fetch articles for each subcategory in priority order
        List<ArticleEntity> prioritizedArticles = new ArrayList<>();
        for (UserPreferencesEntity preference : userPreferences) {
            String subCategoryId = preference.getSubCategory().getSubcategoryId();
            String categoryName = preference.getSubCategory().getCategory().getCategoryName();
            String subCategoryName = preference.getSubCategory().getSubcategoryName();

            // Check if articles are available in the database
            List<ArticleEntity> articlesForSubcategory = articleRepo.findBySubcategoryId(subCategoryId);

            if (!articlesForSubcategory.isEmpty()) {
                // Add articles from the database
                logger.info("Adding articles from DB for subcategory: {}", subCategoryName);
                prioritizedArticles.addAll(articlesForSubcategory);
            } else {
                // Fetch articles from API and save to the database
                logger.info("Fetching articles from API for subcategory: {}", subCategoryName);
                List<ArticleEntity> fetchedArticles = fetchArticlesFromApi(categoryName, subCategoryName);
                if (!fetchedArticles.isEmpty()) {
                    prioritizedArticles.addAll(fetchedArticles);
                }
            }
        }

        return prioritizedArticles;
    }


    @CacheEvict(value = "articlesBySubcategory", key = "#subcategoryId")
    public void evictCacheForSubcategory(String subcategoryId) {
        logger.info("Evicting cache for subcategory ID: {}", subcategoryId);
    }

    @CacheEvict(value = "articlesByPreferences", key = "#userId")
    public void evictCacheForUserPreferences(String userId) {
        logger.info("Evicting cache for user preferences: {}", userId);
    }
    @CacheEvict(value = "articles", key = "#subcategory")
    public void clearCache(String subcategory) {
        logger.info("Clearing cache for subcategory: {}", subcategory);
    }
}
