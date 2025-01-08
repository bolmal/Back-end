package com.example.bolmal.auth.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {


    private JWTUtil jwtUtil;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;


    @Test
    @DisplayName("생성한 토큰의 유효시간은 개발자의 의도와 일치한다")
    public void title(){
        //given
        String category = "access";
        String username = "test";
        String role = "user";
        Long expiredMs = accessTokenValidityInSeconds;


        //when
        String result = jwtUtil.createJwt(category, username, role, expiredMs);

        //then
        Assertions.assertThat(result.get).isEqualTo("Bearer " + result);
    }

}