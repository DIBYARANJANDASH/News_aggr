package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.ArticleEntity;
import com.newsagg_nlp.news_agg.Repo.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepo articleRepo;
    private final RestTemplate restTemplate;

    @Autowired
    public ArticleService(ArticleRepo articleRepo, RestTemplate restTemplate) {
        this.articleRepo = articleRepo;
        this.restTemplate = restTemplate;
    }

    public List<ArticleEntity> fetchArticlesFromApi(String category, String subcategory) {
        String apiUrl = "https://newsapi.org/v2/everything?q=" + subcategory + "&apiKey=c9ecc1e54c544dfa9ffef750dbcc6251";
        ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(apiUrl, NewsApiResponse.class);

        NewsApiResponse newsApiResponse = response.getBody();
        if (newsApiResponse != null && newsApiResponse.getArticles() != null) {
            List<ArticleEntity> articles = newsApiResponse.getArticles();
            // Save articles to MongoDB
//            for (ArticleEntity article : articles) {
////                articleRepo.save(article);
//                articleRepo.insert(article);
//            }
            articleRepo.saveAll(articles);
            return articles; // Return the saved articles
        }
        return null; // Return null if no articles found
    }

    public List<ArticleEntity> getArticlesBySubcategory(Long subcategoryId) {
        return articleRepo.findBySubcategoryId(subcategoryId);
    }
}
