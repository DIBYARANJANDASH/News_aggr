package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepo extends MongoRepository<ArticleEntity, String> {
    List<ArticleEntity> findBySubcategoryId(Long subcategoryId);
}
