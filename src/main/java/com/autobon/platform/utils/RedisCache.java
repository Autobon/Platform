package com.autobon.platform.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * Created by dave on 16/2/17.
 */
@Component
public class RedisCache {
    @Autowired
    private JedisConnectionFactory connectionFactory;

    public void set(byte[] key, byte[] val, long secondsToExpire) {
        connectionFactory.getConnection().setEx(key, secondsToExpire, val);
    }

    public byte[] get(byte[] key) {
        byte[] ret = connectionFactory.getConnection().get(key);
        return ret == null ? new byte[0] : ret;
    }

    public void delete(byte[] key) {
        connectionFactory.getConnection().del(key);
    }

}
