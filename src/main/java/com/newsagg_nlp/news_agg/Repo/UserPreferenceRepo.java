package com.newsagg_nlp.news_agg.Repo;

import com.newsagg_nlp.news_agg.Entity.UserPreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPreferenceRepo extends JpaRepository<UserPreferencesEntity, String> {

    List<UserPreferencesEntity> findByUser_userId(String userId);
}
