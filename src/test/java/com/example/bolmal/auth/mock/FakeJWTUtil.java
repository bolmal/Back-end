package com.example.bolmal.auth.mock;

import com.example.bolmal.auth.jwt.JWTUtil;
import com.example.bolmal.member.service.port.MemberRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FakeJWTUtil implements JWTUtil {

    SecretKey secretKey;

    private FakeCurrentTime fakeCurrentTime=new FakeCurrentTime();

    public FakeJWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    @Override
    public String getUsername(String token) {
        return "";
    }

    @Override
    public String getRole(String token) {
        return "";
    }

    @Override
    public String getCategory(String token) {
        return "";
    }

    @Override
    public Boolean isExpired(String token) {
        return null;
    }

    public Date getExpirationDate(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    @Override
    public String createJwt(String category, String username, String role, Long expiredMs, MemberRepository.CurrentTime fakeCurrentTime) {
        expiredMs = expiredMs * 1000L;

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(fakeCurrentTime.getCurrentTime()))
                .expiration(new Date(fakeCurrentTime.getCurrentTime() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
