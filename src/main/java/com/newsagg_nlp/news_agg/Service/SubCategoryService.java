package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.CategoryEntity;
import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import com.newsagg_nlp.news_agg.Repo.SubCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepo subCategoryRepo;

    public List<SubCategoryEntity> getSubCategoriesByCategory(String categoryId) {
        CategoryEntity category = new CategoryEntity();
        category.setCategoryId(categoryId);
        return subCategoryRepo.findByCategory(category);    }
}
