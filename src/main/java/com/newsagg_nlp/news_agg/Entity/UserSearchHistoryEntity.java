package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_search_history")
public class UserSearchHistoryEntity {
    @Id
    @Column(name = "search_id", columnDefinition = "CHAR(36)")
    private String searchId ;
//            = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String articleTitle;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String articleUrl;

    private LocalDateTime viewedAt = LocalDateTime.now();
}
