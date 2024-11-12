package com.newsagg_nlp.news_agg.dto;

import lombok.Data;

@Data
public class UserPreferenceRequest {
    private String subcategoryId;
    private int priority;

    public UserPreferenceRequest() {
    }

    public UserPreferenceRequest(String subcategoryId, int priority) {
        this.subcategoryId = subcategoryId;
        this.priority = priority;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
