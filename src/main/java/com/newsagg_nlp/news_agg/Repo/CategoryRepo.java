package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity, Long> {
}

