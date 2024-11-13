package com.newsagg_nlp.news_agg.Controller;

import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import com.newsagg_nlp.news_agg.Service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<SubCategoryEntity>> getSubCategories(@PathVariable String categoryId) {
        List<SubCategoryEntity> subCategories = subCategoryService.getSubCategoriesByCategory(categoryId);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }
}
