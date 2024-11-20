package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class CategoryEntity {

    @Id
    @Column(name = "category_id", columnDefinition = "CHAR(36)")
    private String categoryId = UUID.randomUUID().toString();
    private String categoryName;

}
