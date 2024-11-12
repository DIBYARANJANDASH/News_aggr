package com.newsagg_nlp.news_agg.Controller;

import com.newsagg_nlp.news_agg.Entity.ArticleEntity;
import com.newsagg_nlp.news_agg.Entity.UserPreferencesEntity;
import com.newsagg_nlp.news_agg.Service.ArticleService;
import com.newsagg_nlp.news_agg.Service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://127.0.0.1:5500/")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService, UserPreferenceService userPreferenceService) {
        this.articleService = articleService;
    }


    @GetMapping("/articleSub")
    public List<ArticleEntity> getArticlesBySubcategory(@RequestParam String subcategoryId) {
        return articleService.getArticlesBySubcategory(subcategoryId);
    }

    @PostMapping("/fetch")
    public List<ArticleEntity> fetchArticles(@RequestParam String category, @RequestParam String subcategory) {
        List<ArticleEntity> storeArticles = articleService.fetchArticlesFromApi(category, subcategory);
        return new ResponseEntity<>(storeArticles, HttpStatus.CREATED).getBody();
    }

    @GetMapping("/preferences/{userId}")
    public ResponseEntity<List<ArticleEntity>> getArticlesByUserPreferences(@PathVariable String userId) {
        List<ArticleEntity> articles = articleService.getArticlesByUserPreferences(userId);
        if (articles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(articles);
        }
        return ResponseEntity.ok(articles);
    }
}

