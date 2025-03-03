package com.example.bolmalre.common.auth.service;


import com.example.bolmalre.common.auth.infrastructure.port.RefreshRedisEntity;
import com.example.bolmalre.common.auth.infrastructure.redis.RefreshRepository;
import com.example.bolmalre.common.util.RedisIdGenerator;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
@Builder
public class RefreshTokenService {

    private final RefreshRepository refreshRepository;
    private final RedisIdGenerator redisIdGenerator;

    public void addRefreshEntity(String username, String refresh, Long expiredMs) {

        expiredMs = expiredMs * 1000L;

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Long id = redisIdGenerator.generateId(username);

        RefreshRedisEntity refreshToken = RefreshRedisEntity.builder()
                .id(id.toString())
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();


        refreshRepository.save(refreshToken);

    }
}