package com.newsagg_nlp.news_agg.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserFavouritesEntity {
    @Id
    @Column(name = "favourite_id", columnDefinition = "CHAR(36)")
    private String favouriteId;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    private UserEntity user;

    @Column(name = "article_id")
    private String articleId;

    private LocalDateTime favouritedAt;
}
