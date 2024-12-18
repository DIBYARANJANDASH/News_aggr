package com.newsagg_nlp.news_agg.Controller;

import com.newsagg_nlp.news_agg.dto.UserPreferenceDTO;
import com.newsagg_nlp.news_agg.dto.UserPreferenceRequest;
import com.newsagg_nlp.news_agg.Service.UserPreferenceService;
import com.newsagg_nlp.news_agg.dto.UserPreferenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserPreferenceController {
    private final UserPreferenceService userPreferenceService;

    @Autowired
    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    // Updated endpoint to receive subcategoryId and priority
//    @PostMapping("/{userId}")
//    public ResponseEntity<Map<String, Object>> saveUserPreferences(@PathVariable String userId, @RequestBody List<UserPreferenceRequest> preferences) {        userPreferenceService.saveUserPreferences(userId, preferences);
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "success");
//        response.put("message", "User Preferences saved successfully");
//        response.put("userId", userId);
//        response.put("savedPreferences", preferences);  // Optional: return the saved preferences
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
    // Endpoint to retrieve preferences for a specific user
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserPreferenceDTO>> getUserPreferences(@PathVariable String userId) {
        List<UserPreferenceDTO> preferences = userPreferenceService.getUserPreferences(userId);
        return new ResponseEntity<>(preferences, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> saveUserPreferences(
            @PathVariable String userId,
            @RequestBody List<UserPreferenceRequest> preferences) {
        userPreferenceService.saveUserPreferences(userId, preferences);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User Preferences saved successfully");
        response.put("userId", userId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @DeleteMapping("/cache/{userId}")
    @CacheEvict(value = "articlesByPreferences", key = "#userId") // Clear cache for the user
    public ResponseEntity<String> clearUserPreferencesCache(@PathVariable String userId) {
        return ResponseEntity.ok("Cache cleared for user preferences: " + userId);
    }


}

