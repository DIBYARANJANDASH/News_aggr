package com.newsagg_nlp.news_agg.Controller;

import com.newsagg_nlp.news_agg.Entity.ArticleEntity;
import com.newsagg_nlp.news_agg.Service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleEntity> getArticlesBySubcategory(@RequestParam Long subcategoryId) {
        return articleService.getArticlesBySubcategory(subcategoryId);
    }

    @GetMapping("/fetch")
    public List<ArticleEntity> fetchArticles(@RequestParam String category, @RequestParam String subcategory) {
        return articleService.fetchArticlesFromApi(category, subcategory);
    }
}
