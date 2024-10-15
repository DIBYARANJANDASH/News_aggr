package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.UserSearchHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSearchHistoryRepo extends JpaRepository<UserSearchHistoryEntity, Long> {
}
