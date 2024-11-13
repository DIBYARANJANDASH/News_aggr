package com.newsagg_nlp.news_agg.dto;

public class UserPreferenceDTO {
    private String categoryName;
    private String subCategoryName;
    private int priority;

    public UserPreferenceDTO() {
    }

    public UserPreferenceDTO(String categoryName, String subCategoryName, int priority) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.priority = priority;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}