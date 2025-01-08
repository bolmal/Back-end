package com.example.bolmal.auth.service;


import com.example.bolmal.auth.domain.Refresh;
import com.example.bolmal.auth.service.port.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshRepository refreshRepository;


    @Transactional
    public void saveRefresh(String username, Integer expireS, String refresh) {

        Refresh refreshToken = Refresh.toRefresh(username,expireS,refresh);

        refreshRepository.save(refreshToken);
    }
}