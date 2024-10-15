package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.UserFavouritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavouritesRepo extends JpaRepository<UserFavouritesEntity, Long> {
}
