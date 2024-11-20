package com.newsagg_nlp.news_agg.Entity;

import com.newsagg_nlp.news_agg.Entity.UserEntity;
import com.newsagg_nlp.news_agg.Entity.UserSearchHistoryEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "user_favourites")
public class UserFavouritesEntity {
    @Id
    @Column(name = "favourite_id", columnDefinition = "CHAR(36)")
    private String favouriteId = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "search_id", referencedColumnName = "search_id", nullable = false)
    private UserSearchHistoryEntity searchHistory;

    @Column(nullable = false)
    private boolean feedback;
}
