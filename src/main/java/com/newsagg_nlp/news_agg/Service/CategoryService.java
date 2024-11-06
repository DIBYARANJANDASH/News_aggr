package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.CategoryEntity;
import com.newsagg_nlp.news_agg.Repo.CategoryRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo){
        this.categoryRepo = categoryRepo;
    }
    public List<CategoryEntity> getAllCategories(){
        return categoryRepo.findAll();
    }
}
