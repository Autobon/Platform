package com.autobon.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Function;
import java.util.function.Supplier;

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

    public int getInt(String key, int defaultVal) {
        try (Jedis jedis = jedisPool.getResource()) {
            String s = jedis.get(key);
            if (s == null) return defaultVal;
            else return Integer.parseInt(s);
        }
    }

    public String getAfterUpdate(String key, Supplier<String> fallback, int secondsToExpire, Function<String, String> update) {
        try (Jedis jedis = jedisPool.getResource()) {
            String s = jedis.get(key);
            if (s == null) s = fallback.get();

            s = update.apply(s);
            jedis.setex(key, secondsToExpire, "" + s);
            return s;
        }
    }

    public String getOrElse(String key, Supplier<String> fallback, int secondsToExpire) {
        try (Jedis jedis = jedisPool.getResource()) {
            String s = jedis.get(key);
            if (s == null) {
                s = fallback.get();
                jedis.setex(key, secondsToExpire, s);
            }
            return s;
        }
    }

    public void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
    }

}
