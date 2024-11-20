package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Entity.UserFavouritesEntity;
import com.newsagg_nlp.news_agg.Entity.UserSearchHistoryEntity;
import com.newsagg_nlp.news_agg.Repo.UserFavouritesRepo;
import com.newsagg_nlp.news_agg.Repo.UserRepo;
import com.newsagg_nlp.news_agg.Repo.UserSearchHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavouritesService {

    @Autowired
    private UserFavouritesRepo userFavouritesRepo;

    @Autowired
    private UserSearchHistoryRepo userSearchHistoryRepo;

    @Autowired
    private UserRepo userRepo;

    public void saveFavourite(String userId, String searchId, boolean feedback) {
        // Validate searchId exists in the user's search history
        UserSearchHistoryEntity searchHistory = userSearchHistoryRepo.findById(searchId)
                .orElseThrow(() -> new RuntimeException("Search history not found for searchId: " + searchId));

        // Validate user matches the search history
        if (!searchHistory.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("User mismatch for search history");
        }

        // Save feedback
        UserFavouritesEntity favourite = new UserFavouritesEntity();
        favourite.setUser(searchHistory.getUser());
        favourite.setSearchHistory(searchHistory); // Map search history entity
        favourite.setFeedback(feedback);

        userFavouritesRepo.save(favourite);
    }


    public List<UserFavouritesEntity> getUserFavourites(String userId) {
        return userFavouritesRepo.findByUser_UserId(userId);
    }
}

