package com.newsagg_nlp.news_agg.Controller;

import com.newsagg_nlp.news_agg.dto.UserPreferenceRequest;
import com.newsagg_nlp.news_agg.Service.UserPreferenceService;
import com.newsagg_nlp.news_agg.dto.UserPreferenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserPreferenceController {
    private final UserPreferenceService userPreferenceService;

    @Autowired
    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    // Updated endpoint to receive subcategoryId and priority
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> saveUserPreferences(@PathVariable String userId, @RequestBody List<UserPreferenceRequest> preferences) {        userPreferenceService.saveUserPreferences(userId, preferences);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User Preferences saved successfully");
        response.put("userId", userId);
        response.put("savedPreferences", preferences);  // Optional: return the saved preferences

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
