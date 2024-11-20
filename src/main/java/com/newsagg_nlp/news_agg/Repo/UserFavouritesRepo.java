package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.UserFavouritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavouritesRepo extends JpaRepository<UserFavouritesEntity, String> {
    List<UserFavouritesEntity> findByUser_UserId(String userId);
}
