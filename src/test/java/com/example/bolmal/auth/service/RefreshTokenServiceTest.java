package com.example.bolmal.auth.service;

import com.example.bolmal.auth.domain.Refresh;
import com.example.bolmal.auth.jwt.JWTUtilImpl;
import com.example.bolmal.auth.mock.FakeCurrentTime;
import com.example.bolmal.auth.mock.FakeJWT;
import com.example.bolmal.auth.mock.FakeJWTUtil;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;


class RefreshTokenServiceTest {
    /**
     *
     * fakeRefreshRepsoiutory 만들고 처음부터 다시 만들기
     *
     * */

    private RefreshTokenService refreshTokenService;

    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    long refreshTokenValidityInSeconds;

    FakeCurrentTime fakeCurrentTime= new FakeCurrentTime();

    @BeforeEach
    void setUp() {
        FakeCurrentTime fakeCurrentTime = new FakeCurrentTime();

        this.refreshTokenService = RefreshTokenService.builder()
                .currentTime(fakeCurrentTime)
                .build();

        String secret = "fekjrvehfdnsdkejrfehvfnmdskwqfejvrfehnfewrfgfrew";
        FakeJWTUtil fakeJWTUtil = new FakeJWTUtil(secret);


        String username = "test";
        String category = "refresh";
        String role = "user";

        String jwt = fakeJWTUtil.createJwt(username, category, role, refreshTokenValidityInSeconds,fakeCurrentTime);
        Date expirationDate = fakeJWTUtil.getExpirationDate(jwt);
    }



    @Test
    @DisplayName("생성한 토큰의 유효시간은 개발자의 의도와 일치한다")
    public void title() {
        // given
        FakeCurrentTime fakeCurrentTime = new FakeCurrentTime();

        this.refreshTokenService = RefreshTokenService.builder()
                .currentTime(fakeCurrentTime)
                .build();

        String secret = "fekjrvehfdnsdkejrfehvfnmdskwqfejvrfehnfewrfgfrew";
        FakeJWTUtil fakeJWTUtil = new FakeJWTUtil(secret);


        String username = "test";
        String category = "refresh";
        String role = "user";

        String jwt = fakeJWTUtil.createJwt(username, category, role, refreshTokenValidityInSeconds,fakeCurrentTime);

        // 현재 시간을 기준으로 예상 만료 시간 계산
        long currentTimeMillis =fakeCurrentTime.getCurrentTime();
        long expectedExpirationMillis = currentTimeMillis + refreshTokenValidityInSeconds * 1000L;
        String expectedExpiration = new Date(expectedExpirationMillis).toString();

        // when
        Refresh result = refreshTokenService.addRefreshEntity(username, jwt, refreshTokenValidityInSeconds);

        // then
        Assertions.assertThat(result.getExpiration())
                .isEqualTo(expectedExpiration);
    }

}