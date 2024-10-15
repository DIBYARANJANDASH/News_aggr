package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class SubCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subcategoryId;
    private String subcategoryName;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private CategoryEntity category;
}
