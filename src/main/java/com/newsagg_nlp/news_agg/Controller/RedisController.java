package com.newsagg_nlp.news_agg.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis-test")
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping
    public String testRedis() {
        // Set a key
        redisTemplate.opsForValue().set("testKey", "Redis is working!");
        // Get the key
        return redisTemplate.opsForValue().get("testKey");
    }
}
