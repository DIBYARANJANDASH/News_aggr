package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
}


