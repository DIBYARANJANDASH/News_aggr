package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.SubCategoryEntity;
import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Entity.UserPreferencesEntity;
import com.newsagg_nlp.news_agg.Repo.SubCategoryRepo;
import com.newsagg_nlp.news_agg.Repo.UserPreferenceRepo;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import com.newsagg_nlp.news_agg.dto.UserPreferenceDTO;
import com.newsagg_nlp.news_agg.dto.UserPreferenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
//    public void saveUserPreferences(String userId, List<UserPreferenceRequest> preferences) {
//        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//        for (UserPreferenceRequest preferenceRequest : preferences) {
//            SubCategoryEntity subCategory = subCategoryRepo.findById(preferenceRequest.getSubcategoryId())
//                    .orElseThrow(() -> new RuntimeException("Subcategory not found"));
//
//            boolean exists = userPreferenceRepo.existsByUserAndSubCategory(user, subCategory);
//            if (!exists) {
//                UserPreferencesEntity preference = new UserPreferencesEntity();
//                preference.setUser(user);
//                preference.setSubCategory(subCategory);
//                preference.setPriority(preferenceRequest.getPriority());
//                preference.setCreatedAt(LocalDateTime.now());
//                preference.setUpdatedAt(LocalDateTime.now());
//
//                userPreferenceRepo.save(preference);
//            }
//        }
//    }

    public List<UserPreferenceDTO> getUserPreferences(String userId) {
        List<UserPreferencesEntity> preferences = userPreferenceRepo.findByUser_userId(userId);

        return preferences.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserPreferenceDTO convertToDTO(UserPreferencesEntity preference) {
        UserPreferenceDTO dto = new UserPreferenceDTO();
        dto.setCategoryName(preference.getSubCategory().getCategory().getCategoryName());  // Ensure correct getter chain
        dto.setSubCategoryName(preference.getSubCategory().getSubcategoryName());
        dto.setSubCategoryId(preference.getSubCategory().getSubcategoryId());
        dto.setPriority(preference.getPriority());
        return dto;
    }

    @CacheEvict(value = "articlesByPreferences", key = "#userId")
    public void saveUserPreferences(String userId, List<UserPreferenceRequest> preferences) {
        // Validate input
        if (preferences == null || preferences.isEmpty()) {
            throw new IllegalArgumentException("Preferences list cannot be null or empty.");
        }

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        preferences.forEach(pref -> {
            if (pref.getSubcategoryId() == null || pref.getPriority() <= 0) {
                throw new IllegalArgumentException("Invalid preference: " + pref);
            }

            SubCategoryEntity subCategory = subCategoryRepo.findById(pref.getSubcategoryId())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found"));

            UserPreferencesEntity preference = userPreferenceRepo.findByUserAndSubCategory(user, subCategory)
                    .orElseGet(UserPreferencesEntity::new);

            preference.setUser(user);
            preference.setSubCategory(subCategory);
            preference.setPriority(pref.getPriority());
            preference.setUpdatedAt(LocalDateTime.now());

            if (preference.getCreatedAt() == null) {
                preference.setCreatedAt(LocalDateTime.now());
            }

            userPreferenceRepo.save(preference);
        });


    }

}

