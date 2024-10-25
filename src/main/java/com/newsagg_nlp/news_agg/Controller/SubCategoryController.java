package com.newsagg_nlp.news_agg.Controller;

import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import com.newsagg_nlp.news_agg.Service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping("/subcategories/{categoryId}")
    public ResponseEntity<List<SubCategoryEntity>> getSubCategories(@PathVariable Long categoryId) {
        List<SubCategoryEntity> subCategories = subCategoryService.getSubCategoriesByCategory(categoryId);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }
}
