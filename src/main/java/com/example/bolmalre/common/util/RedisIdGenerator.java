package com.example.bolmalre.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisIdGenerator {

    private final StringRedisTemplate redisTemplate;

    public RedisIdGenerator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long generateId(String key) {
        // Redis에서 key에 대해 Auto Increment 수행
        return redisTemplate.opsForValue().increment(key);
    }
}
