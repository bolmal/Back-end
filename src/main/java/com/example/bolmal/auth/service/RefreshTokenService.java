package com.example.bolmal.auth.service;


import com.example.bolmal.auth.domain.Refresh;
import com.example.bolmal.auth.service.port.RefreshRepository;
import com.example.bolmal.config.JWTConfig;
import com.example.bolmal.member.util.CurrentTime;
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
    private final CurrentTime currentTime;

    public Refresh addRefreshEntity(String username, String refresh, Long expiredMs) {

        expiredMs = expiredMs * 1000L;

        Date date = new Date(currentTime.getCurrentTime() + expiredMs);

        Refresh refreshToken = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshToken);

        return refreshToken;
    }
}