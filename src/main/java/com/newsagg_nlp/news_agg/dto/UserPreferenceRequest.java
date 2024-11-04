package com.newsagg_nlp.news_agg.dto;

import lombok.Data;

@Data
public class UserPreferenceRequest {
    private String subcategoryId;
    private int priority;
}
