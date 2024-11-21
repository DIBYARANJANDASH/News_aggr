//package com.newsagg_nlp.news_agg.Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RedisService {
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    // Save data in Redis
//    public void save(String key, Object value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    // Get data from Redis
//    public Object get(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//}
