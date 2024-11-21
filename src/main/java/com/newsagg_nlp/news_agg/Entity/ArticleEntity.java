package com.newsagg_nlp.news_agg.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;

@Data
@Document(collection = "articles")
public class ArticleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String articleId;
    private String title;
    private String description;
    private String content;
    private URL url;
    @JsonProperty("image")
    private URL imageUrl;

    private Source source;
    private LocalDateTime publishedAt;
    private String author;
    private String subcategoryId;

    // Nested Source class to match the API response structure
    @Data
    public static class Source implements Serializable {
        private String id;
        private String name;
    }
}
