package com.newsagg_nlp.news_agg.Entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.time.LocalDateTime;


@Data
@Document(collection = "articles")
public class ArticleEntity {
    @Id
    private String articleId;
    private String title;
    private String description;
    private String content;

    private URL url;
    private URL imageUrl;

    private String source;
    private LocalDateTime publishedAt;
    private String author;

    private Long subcategoryId;

}
