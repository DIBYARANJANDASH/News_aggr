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

    private static final Duration EXPIRY_DURATION = Duration.ofDays(2); // Expiry duration for articles

    @Autowired
    public ArticleService(ArticleRepo articleRepo, SubCategoryRepo subCategoryRepo, RestTemplate restTemplate,
                          UserPreferenceRepo userPreferenceRepo) {
        this.articleRepo = articleRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.restTemplate = restTemplate;
        this.userPreferenceRepo = userPreferenceRepo;
    }

    /**
     * Fetch articles from an API and store them in the database if they are outdated.
     *
     * @param category    The category parameter for the API.
     * @param subcategory The subcategory parameter for the API.
     * @return List of saved ArticleEntity objects.
     */
    @Cacheable(value = "articles", key = "#subcategory")
    public List<ArticleEntity> fetchArticlesFromApi(String category, String subcategory) {
        logger.info("Fetching articles for subcategory: {}", subcategory);

        Optional<SubCategoryEntity> subCategoryEntityOpt = Optional.ofNullable(subCategoryRepo.findBySubcategoryName(subcategory));
        if (subCategoryEntityOpt.isEmpty()) {
            logger.warn("Subcategory ID not found for: {}", subcategory);
            return List.of(); // Return an empty list if subcategory is not found
        }

        String subcategoryId = subCategoryEntityOpt.get().getSubcategoryId();

        // Check if existing articles for this subcategory are outdated
        boolean isDataOutdated = articleRepo.findBySubcategoryId(subcategoryId).stream()
                .map(ArticleEntity::getPublishedAt)
                .max(LocalDateTime::compareTo)
                .map(latestDate -> Duration.between(latestDate, LocalDateTime.now()).compareTo(EXPIRY_DURATION) > 0)
                .orElse(true);

        if (!isDataOutdated) {
            logger.info("Using cached articles for subcategory: {}", subcategory);
            return articleRepo.findBySubcategoryId(subcategoryId); // Return existing articles if they are not outdated
        }

        // If data is outdated, fetch new articles from the API
        String apiUrl = "https://gnews.io/api/v4/search?q=" + subcategory + "&apikey=b30033fd80f44a50cec737303c807bd8&lang=en";
        ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(apiUrl, NewsApiResponse.class);

        NewsApiResponse newsApiResponse = response.getBody();
        if (newsApiResponse != null && newsApiResponse.getArticles() != null) {
            List<ArticleEntity> newArticles = newsApiResponse.getArticles();

            // Set subcategoryId for each article
            newArticles.forEach(article -> article.setSubcategoryId(subcategoryId));

            // Filter only new articles to avoid duplication
            List<ArticleEntity> existingArticles = articleRepo.findBySubcategoryId(subcategoryId);
            List<ArticleEntity> articlesToSave = newArticles.stream()
                    .filter(newArticle -> existingArticles.stream()
                            .noneMatch(existing -> existing.getTitle().equals(newArticle.getTitle())))
                    .collect(Collectors.toList());

            if (!articlesToSave.isEmpty()) {
                logger.info("Saving new articles for subcategory: {}", subcategory);
                articleRepo.saveAll(articlesToSave); // Save only non-duplicate articles
            } else {
                logger.info("No new articles to save for subcategory: {}", subcategory);
            }

            return articleRepo.findBySubcategoryId(subcategoryId); // Return all articles
        }

        logger.warn("No articles fetched from API for subcategory: {}", subcategory);
        return List.of();
    }

    /**
     * Get articles based on user preferences, fetching new ones if the existing ones are outdated.
     *
     * @param userId The ID of the user whose preferences will be used to fetch articles.
     * @return List of articles based on user preferences.
     */
    @Cacheable(value = "articlesBySubcategory", key = "#subcategoryId")
    public List<ArticleEntity> getArticlesBySubcategory(String subcategoryId) {
        logger.info("Fetching articles for subcategory ID: {}", subcategoryId);
        return articleRepo.findBySubcategoryId(subcategoryId);
    }

    @Cacheable(value = "articlesByPreferences", key = "#userId")
    public List<ArticleEntity> getArticlesByUserPreferences(String userId) {
        logger.info("Fetching articles for user ID: {}", userId);

        // Fetch articles based on user preferences
        List<UserPreferencesEntity> userPreferences = userPreferenceRepo.findByUser_userId(userId);
        if (userPreferences.isEmpty()) {
            logger.warn("No preferences found for user ID: {}", userId);
            return List.of();
        }

        Set<String> uniqueSubCategoryIds = userPreferences.stream()
                .map(preference -> preference.getSubCategory().getSubcategoryId())
                .collect(Collectors.toSet());

        return uniqueSubCategoryIds.stream()
                .flatMap(subcategoryId -> fetchArticlesFromApi(
                        subCategoryRepo.findById(subcategoryId).get().getCategory().getCategoryName(),
                        subCategoryRepo.findById(subcategoryId).get().getSubcategoryName()
                ).stream())
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "articlesBySubcategory", key = "#subcategoryId")
    public void evictCacheForSubcategory(String subcategoryId) {
        logger.info("Evicting cache for subcategory ID: {}", subcategoryId);
        // Method to clear cache for a specific subcategory
    }

    @CacheEvict(value = "articlesByPreferences", key = "#userId")
    public void evictCacheForUserPreferences(String userId) {
        logger.info("Evicting cache for user ID: {}", userId);
        // Method to clear cache for user preferences
    }
}
