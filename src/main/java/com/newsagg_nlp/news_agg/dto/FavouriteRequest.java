package com.newsagg_nlp.news_agg.dto;

import lombok.Data;

@Data
public class FavouriteRequest {
    private String searchId;
    private boolean feedback;
}