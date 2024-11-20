package com.newsagg_nlp.news_agg.Controller;

import com.newsagg_nlp.news_agg.Entity.UserFavouritesEntity;
import com.newsagg_nlp.news_agg.Entity.UserSearchHistoryEntity;
import com.newsagg_nlp.news_agg.Service.UserFavouritesService;
import com.newsagg_nlp.news_agg.Service.UserSearchHistoryService;
import com.newsagg_nlp.news_agg.dto.ArticleHistoryRequest;
import com.newsagg_nlp.news_agg.dto.FavouriteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserActivityController {

    @Autowired
    private UserSearchHistoryService userSearchHistoryService;

    @Autowired
    private UserFavouritesService userFavouritesService;

    // Save search history
    @PostMapping("/{userId}/history")
    public ResponseEntity<String> saveSearchHistory(
            @PathVariable String userId,
            @RequestBody ArticleHistoryRequest request) {
        UserSearchHistoryEntity savedHistory = userSearchHistoryService.saveSearchHistory(
                userId,
                request.getSearchId(),
                request.getTitle(),
                request.getDescription(),
                request.getUrl()
        );
        return ResponseEntity.ok(savedHistory.getSearchId());
    }

    // Get search history
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<UserSearchHistoryEntity>> getSearchHistory(@PathVariable String userId) {
        return ResponseEntity.ok(userSearchHistoryService.getSearchHistory(userId));
    }

    // Save user favourite
    @PostMapping("/{userId}/favourites")
    public ResponseEntity<Void> saveFavourite(
            @PathVariable String userId,
            @RequestBody FavouriteRequest request) {
        try {
            userFavouritesService.saveFavourite(userId, request.getSearchId(), request.isFeedback());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    // Get user favourites
    @GetMapping("/{userId}/favourites")
    public ResponseEntity<List<UserFavouritesEntity>> getUserFavourites(@PathVariable String userId) {
        return ResponseEntity.ok(userFavouritesService.getUserFavourites(userId));
    }
}


