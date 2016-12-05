package com.autobon.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by dave on 16/2/17.
 */
@Configuration
public class RedisCacheConfig {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.password:\"\"}")
    private String redisPassword;

    @Bean
    public JedisPool createJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(2);
        config.setMaxIdle(5);
        config.setTestOnBorrow(true);
        config.setMaxTotal(8);
        config.setMaxWaitMillis(5000);

        if ("".equals(redisPassword)) {
            return new JedisPool(config, redisHost, redisPort);
        } else {
            return new JedisPool(config, redisHost, redisPort, 3000, redisPassword);
        }

    }

}
