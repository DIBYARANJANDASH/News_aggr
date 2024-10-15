package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserSearchHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;
    private String query;
    private LocalDateTime searchTime;
}
