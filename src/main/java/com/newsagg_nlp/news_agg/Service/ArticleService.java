package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.ArticleEntity;
import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import com.newsagg_nlp.news_agg.Entity.UserPreferencesEntity;
import com.newsagg_nlp.news_agg.Repo.ArticleRepo;
import com.newsagg_nlp.news_agg.Repo.SubCategoryRepo;
import com.newsagg_nlp.news_agg.Repo.UserPreferenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {

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
    public List<ArticleEntity> fetchArticlesFromApi(String category, String subcategory) {
        Optional<SubCategoryEntity> subCategoryEntityOpt = Optional.ofNullable(subCategoryRepo.findBySubcategoryName(subcategory));
        if (subCategoryEntityOpt.isEmpty()) {
            System.out.println("Subcategory ID not found for: " + subcategory);
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
            // Return existing articles if they are not outdated
            return articleRepo.findBySubcategoryId(subcategoryId);
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

            articleRepo.saveAll(articlesToSave); // Save only non-duplicate articles
            return articleRepo.findBySubcategoryId(subcategoryId); // Return all articles
        }
        return List.of();
    }

    /**
     * Get articles based on user preferences, fetching new ones if the existing ones are outdated.
     *
     * @param userId The ID of the user whose preferences will be used to fetch articles.
     * @return List of articles based on user preferences.
     */
    public List<ArticleEntity> getArticlesByUserPreferences(String userId) {
        // Fetch unique user preferences based on subcategoryId
        List<UserPreferencesEntity> userPreferences = userPreferenceRepo.findByUser_userId(userId);

        if (userPreferences.isEmpty()) {
            System.out.println("No preferences found for user with ID: " + userId);
            return List.of(); // Return empty list if no preferences
        }

        // Extract unique subcategory IDs from preferences
        Set<String> uniqueSubCategoryIds = userPreferences.stream()
                .map(preference -> preference.getSubCategory().getSubcategoryId())
                .collect(Collectors.toSet());

        // Fetch articles for each preferred subcategory, dynamically getting the category name
        return uniqueSubCategoryIds.stream()
                .flatMap(subcategoryId -> {
                    try {
                        // Retrieve the category name for each subcategory
                        SubCategoryEntity subCategory = subCategoryRepo.findById(subcategoryId)
                                .orElseThrow(() -> new RuntimeException("Subcategory not found: " + subcategoryId));

                        String categoryName = subCategory.getCategory().getCategoryName();
                        return fetchArticlesFromApi(categoryName, subCategory.getSubcategoryName()).stream();
                    } catch (Exception e) {
                        System.err.println("Failed to fetch articles for subcategory: " + subcategoryId);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Fetch articles by a specific subcategory ID.
     *
     * @param subcategoryId The subcategory ID to filter articles.
     * @return List of articles filtered by subcategory.
     */
    public List<ArticleEntity> getArticlesBySubcategory(String subcategoryId) {
        return articleRepo.findBySubcategoryId(subcategoryId);
    }
}
