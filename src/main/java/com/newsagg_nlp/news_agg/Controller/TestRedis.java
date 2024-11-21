package com.newsagg_nlp.news_agg.Controller;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Connection;

public class TestRedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://default:*******@redis-13763.c253.us-central1-1.gce.redns.redis-cloud.com:13763");
        Connection connection = jedis.getConnection();
        System.out.println(jedis);
    }
}
