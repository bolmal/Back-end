package com.example.bolmalre.auth.jwt;

import org.springframework.stereotype.Component;

@Component
public interface JWTUtil {
    String getUsername(String token);

    String getRole(String token);

    //카테고리 추출
    String getCategory(String token);

    Boolean isExpired(String token);

    String createJwt(String category, String username, String role, Long expiredMs);
}
