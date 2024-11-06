package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Entity.UserPreferencesEntity;
import com.newsagg_nlp.news_agg.Repo.SubCategoryRepo;
import com.newsagg_nlp.news_agg.Repo.UserPreferenceRepo;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import com.newsagg_nlp.news_agg.dto.UserPreferenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserPreferenceService {
    private final UserPreferenceRepo userPreferenceRepo;
    private final UserRepo userRepo;
    private final SubCategoryRepo subCategoryRepo;

    @Autowired
    public UserPreferenceService(UserPreferenceRepo userPreferenceRepo, UserRepo userRepo, SubCategoryRepo subCategoryRepo) {
        this.userPreferenceRepo = userPreferenceRepo;
        this.userRepo = userRepo;
        this.subCategoryRepo = subCategoryRepo;
    }

    // Updated method to include priority
    public void saveUserPreferences(String userId, List<UserPreferenceRequest> preferences) {
        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        for (UserPreferenceRequest preferenceRequest : preferences) {
            SubCategoryEntity subCategory = subCategoryRepo.findById(preferenceRequest.getSubcategoryId())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found"));

            UserPreferencesEntity preference = new UserPreferencesEntity();
            preference.setUser(user);
            preference.setSubCategory(subCategory);
            preference.setPriority(preferenceRequest.getPriority());
            preference.setCreatedAt(LocalDateTime.now());
            preference.setUpdatedAt(LocalDateTime.now());

            userPreferenceRepo.save(preference);
        }
    }
}
