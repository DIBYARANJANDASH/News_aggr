package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class UserPreferencesEntity {
    @Id
    @Column(name = "preference_id", columnDefinition = "CHAR(36)")
    private String preferenceId = UUID.randomUUID().toString();;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", columnDefinition = "CHAR(36)")
    private SubCategoryEntity subCategory;

    private int priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
