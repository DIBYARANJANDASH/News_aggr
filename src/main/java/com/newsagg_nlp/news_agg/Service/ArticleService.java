package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.ArticleEntity;
import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Entity.UserPreferencesEntity;
import com.newsagg_nlp.news_agg.Repo.ArticleRepo;
import com.newsagg_nlp.news_agg.Repo.SubCategoryRepo;
import com.newsagg_nlp.news_agg.Repo.UserPreferenceRepo;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepo articleRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final UserPreferenceRepo userPreferenceRepo;
    private final RestTemplate restTemplate;
    private final UserRepo userRepo;

    @Autowired
    public ArticleService(ArticleRepo articleRepo, SubCategoryRepo subCategoryRepo, RestTemplate restTemplate, UserPreferenceRepo userPreferenceRepo, UserRepo userRepo) {
        this.articleRepo = articleRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.restTemplate = restTemplate;
        this.userPreferenceRepo = userPreferenceRepo;
        this.userRepo = userRepo;
    }

    /**
     * Fetch articles from an API and assign subcategory IDs.
     * @param category The category parameter for the API.
     * @param subcategory The subcategory parameter for the API.
     * @return List of saved ArticleEntity objects.
     */
    public List<ArticleEntity> fetchArticlesFromApi(String category, String subcategory) {
        String apiUrl = "https://newsapi.org/v2/everything?q=" + subcategory + "&apiKey=c9ecc1e54c544dfa9ffef750dbcc6251";
        ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(apiUrl, NewsApiResponse.class);

        NewsApiResponse newsApiResponse = response.getBody();
        if (newsApiResponse != null && newsApiResponse.getArticles() != null) {
            List<ArticleEntity> articles = newsApiResponse.getArticles();

            // Fetch the subcategory entity from the database
            SubCategoryEntity subCategoryEntity = subCategoryRepo.findBySubcategoryName(subcategory);
            Long subcategoryId = (subCategoryEntity != null) ? subCategoryEntity.getSubcategoryId() : null;

            if (subcategoryId == null) {
                System.out.println("Subcategory ID not found for: " + subcategory);
            }

            // Set subcategoryId for each article
            for (ArticleEntity article : articles) {
                article.setSubcategoryId(subcategoryId);
            }

            // Save all articles to MongoDB
            articleRepo.saveAll(articles);
            return articles;
        }
        return null;
    }

    public List<ArticleEntity> getArticlesByUserPreferences(Long userId) {
        // Fetch the user
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch user preferences
        List<UserPreferencesEntity> userPreferences = userPreferenceRepo.findByUser_userId(userId);

        // Extract subcategory IDs from preferences
        List<Integer> subCategoryIds = userPreferences.stream()
                .map(preference -> preference.getSubCategory().getSubcategoryId().intValue())
                .collect(Collectors.toList());

        // Fetch articles from MongoDB based on subcategory IDs
        return articleRepo.findBySubcategoryIdIn(subCategoryIds);
    }

    /**
     * Fetch articles by a specific subcategory ID.
     * @param subcategoryId The subcategory ID to filter articles.
     * @return List of articles filtered by subcategory.
     */
    public List<ArticleEntity> getArticlesBySubcategory(Long subcategoryId) {
        return articleRepo.findBySubcategoryId(subcategoryId);
    }
}
