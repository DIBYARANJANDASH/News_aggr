package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.UserPreferencesEntity;
import com.newsagg_nlp.news_agg.Repo.UserPreferenceRepo;

import java.util.List;

public class UserPreferenceService {
    private final UserPreferenceRepo userPreferenceRepo;

    public UserPreferenceService(UserPreferenceRepo userPreferenceRepo){
        this.userPreferenceRepo = userPreferenceRepo;

    }

    public UserPreferencesEntity saveUserPreferences(UserPreferencesEntity userPreferences){
        return userPreferenceRepo.save(userPreferences);
    }
    // Get preferences for a user
    public List<UserPreferencesEntity> getPreferencesByUserId(String userId) {
        return userPreferenceRepo.findByUser_userId(userId);
    }

    // Delete user preferences by ID
    public void deleteUserPreferences(String preferenceId) {
        userPreferenceRepo.deleteById(preferenceId);
    }

}
