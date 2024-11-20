package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.UserSearchHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSearchHistoryRepo extends JpaRepository<UserSearchHistoryEntity, String> {
    List<UserSearchHistoryEntity> findByUser_UserId(String userId);
}
