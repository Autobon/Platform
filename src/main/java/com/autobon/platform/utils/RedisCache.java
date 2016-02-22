package com.autobon.platform.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by dave on 16/2/17.
 */
@Component
public class RedisCache {
    @Autowired
    private Jedis jedis;

    public void set(String key, String value, int secondsToExpire) {
        jedis.setex(key, secondsToExpire, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public void delete(String key) {
        jedis.del(key);
    }

}
