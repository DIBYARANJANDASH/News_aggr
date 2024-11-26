package com.newsagg_nlp.news_agg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class NewsAggApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsAggApplication.class, args);
	}

}
