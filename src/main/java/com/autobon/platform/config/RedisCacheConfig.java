package com.autobon.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * Created by dave on 16/2/17.
 */
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public Jedis getJedis() {
        return new Jedis(redisHost, redisPort);
    }

//    @Bean
//    public JedisConnectionFactory redisConnectionFactory() {
//        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
//        redisConnectionFactory.setHostName(redisHost);
//        redisConnectionFactory.setPort(redisPort);
//        redisConnectionFactory.setTimeout(10000);
////        JedisPoolConfig poolConfig = new JedisPoolConfig();
////        poolConfig.setMaxTotal(3);
////        poolConfig.setMaxIdle(3);
////        poolConfig.setMinIdle(1);
////        poolConfig.setMaxWaitMillis(10000);
////        //poolConfig.setTestOnBorrow(true);
////        redisConnectionFactory.setPoolConfig(poolConfig);
//        return redisConnectionFactory;
//    }

//    @Bean
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(cf);
//        return redisTemplate;
//    }

//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//
//        // Number of seconds before expiration. Defaults to unlimited (0)
//        cacheManager.setDefaultExpiration(3000); // Sets the default expire time (in seconds)
//        return cacheManager;
//    }
}
