package com.autobon.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by dave on 16/2/17.
 */
@Component
public class RedisCache {
    @Autowired
    private JedisPool jedisPool;

    public void set(String key, String value, int secondsToExpire) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, secondsToExpire, value);
        }
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
    }

}
