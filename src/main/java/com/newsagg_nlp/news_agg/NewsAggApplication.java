package com.newsagg_nlp.news_agg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NewsAggApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsAggApplication.class, args);
	}

}
