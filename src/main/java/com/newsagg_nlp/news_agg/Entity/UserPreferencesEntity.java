package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
public class UserPreferencesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    private Long subcategoryId;
    private int priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
