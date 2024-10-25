package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class SubCategoryEntity {

    @Id
    @Column(name = "subcategory_id", columnDefinition = "CHAR(36)")
    private String subcategoryId = UUID.randomUUID().toString();
    private String subcategoryName;

    @ManyToOne
    @JoinColumn(name = "categoryId", columnDefinition = "CHAR(36)")
    private CategoryEntity category;
}
