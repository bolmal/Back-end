package com.example.bolmal.auth.service;

import com.example.bolmal.auth.jwt.JWTUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

class RefreshTokenServiceTest {


    private RefreshTokenService refreshTokenService;
    private JWTUtil jwtUtil;




    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;


    @Test
    @DisplayName("생성한 토큰의 유효시간은 개발자의 의도와 일치한다")
    public void title(){
        //given
        String category = "refresh";
        String username = "test";
        String role = "user";
        Long expiredMs = refreshTokenValidityInSeconds;

        String jwt = jwtUtil.createJwt(category, username, role, expiredMs);


        //when
        refreshTokenService.addRefreshEntity(category,jwt,);

        //then
        Assertions.assertThat(result.get).isEqualTo("Bearer " + result);
    }

}