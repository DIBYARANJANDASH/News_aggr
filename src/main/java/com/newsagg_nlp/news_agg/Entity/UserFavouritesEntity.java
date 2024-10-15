package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserFavouritesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favouriteId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @Column(name = "articleId")
    private String articleId;
    private LocalDateTime favouritedAt;


}
