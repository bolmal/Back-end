package com.example.bolmal.auth.mock;

import com.example.bolmal.auth.jwt.JWTUtil;
import com.example.bolmal.auth.service.port.CurrentTime;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FakeJWTUtil implements JWTUtil {

    SecretKey secretKey;

    public FakeJWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
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
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    @Override
    public String createJwt(String category, String username, String role, Long expiredMs, CurrentTime fakeCurrentTime) {
        expiredMs = expiredMs * 1000L;

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(new Date(fakeCurrentTime.getCurrentTime()))
                .setExpiration(new Date(fakeCurrentTime.getCurrentTime() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
