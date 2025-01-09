package com.example.bolmal.auth.service;


import com.example.bolmal.auth.domain.Refresh;
import com.example.bolmal.auth.mock.FakeCurrentTime;
import com.example.bolmal.auth.mock.FakeJWTUtil;
import com.example.bolmal.auth.mock.FakeRefreshRepository;
import com.example.bolmal.auth.service.port.CurrentTime;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

class RefreshTokenServiceTest {

    private RefreshTokenService refreshTokenService;
    private FakeJWTUtil fakeJWTUtil;
    private final FakeCurrentTime fakeCurrentTime = new FakeCurrentTime();

    @BeforeEach
    void setUp() {

        String secret = "fekjrvehfdnsdkejrfehvfnmdskwqfejvrfehnfewrfgfrew";
        FakeRefreshRepository fakeRefreshRepository = new FakeRefreshRepository();

        this.refreshTokenService = RefreshTokenService
                .builder()
                .refreshRepository(fakeRefreshRepository)
                .build();

        this.fakeJWTUtil = new FakeJWTUtil(secret);

    }


    @Test
    @DisplayName("addRefresh() 를 이용하여 만든 리프레시 토큰의 유효시간은 개발자의 의도와 일치한다")
    public void title(){
        //given
        String username = "test";
        String category = "refresh";
        String role = "USER";
        Long expiredMs = 1234560L;

        String jwt = fakeJWTUtil.createJwt(username, category, role, expiredMs, fakeCurrentTime);

        //when
        Refresh refreshEntity = refreshTokenService.addRefreshEntity(username, jwt, expiredMs, fakeCurrentTime);

        String expiration = refreshEntity.getExpiration();
        Date date = new Date(fakeCurrentTime.getCurrentTime() + expiredMs * 1000L);
        String exExpiration = date.toString();

        //then

        Assertions.assertThat(expiration).isEqualTo(exExpiration);
    }

}