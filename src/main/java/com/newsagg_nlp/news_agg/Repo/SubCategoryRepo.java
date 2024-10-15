package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.CategoryEntity;
import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategoryEntity, Long> {
    List<SubCategoryEntity> findByCategory(CategoryEntity category);
}
