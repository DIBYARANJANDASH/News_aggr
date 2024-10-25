package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class UserSearchHistoryEntity {
    @Id
    @Column(name = "search_id", columnDefinition = "CHAR(36)")
    private String searchId = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "user_id", columnDefinition = "CHAR(36)")
    private UserEntity user;
    private String query;
    private LocalDateTime searchTime;
}
