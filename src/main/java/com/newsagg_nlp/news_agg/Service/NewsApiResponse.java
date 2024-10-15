package com.newsagg_nlp.news_agg.Service;

import com.newsagg_nlp.news_agg.Entity.ArticleEntity;
import lombok.Data;

import java.util.List;

@Data
public class NewsApiResponse {
    private String status;
    private int totalResults;
    private List<ArticleEntity> articles;
}
