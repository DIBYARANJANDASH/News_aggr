package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Entity.UserSearchHistoryEntity;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import com.newsagg_nlp.news_agg.Repo.UserSearchHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserSearchHistoryService {

    @Autowired
    private UserSearchHistoryRepo userSearchHistoryRepo;

    @Autowired
    private UserRepo userRepo;

    public UserSearchHistoryEntity saveSearchHistory(String userId, String searchId ,String title, String description, String url) {
        // Validate user exists
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));

        // Validate inputs
        if (title == null || title.isEmpty()) {
            throw new RuntimeException("Title cannot be null or empty");
        }
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("URL cannot be null or empty");
        }

        // Create history object
        UserSearchHistoryEntity history = new UserSearchHistoryEntity();
        history.setSearchId(searchId);
        history.setUser(user);
        history.setArticleTitle(title);
        history.setDescription(description != null ? description : "No description provided");
        history.setArticleUrl(url);
        history.setViewedAt(LocalDateTime.now());

        // Save to database
        return userSearchHistoryRepo.save(history);
    }



    public List<UserSearchHistoryEntity> getSearchHistory(String userId) {
        return userSearchHistoryRepo.findByUser_UserId(userId);
    }
}
