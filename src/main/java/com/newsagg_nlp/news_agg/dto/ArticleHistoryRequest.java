package com.newsagg_nlp.news_agg.dto;

import lombok.Data;

@Data
public class ArticleHistoryRequest {
    private String searchId;
    private String title;
    private String description;
    private String url;
}
