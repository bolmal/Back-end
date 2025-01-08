package com.example.bolmal.auth.jwt;

import com.example.bolmal.member.util.CurrentTime;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtilImpl implements JWTUtil {

    private SecretKey secretKey;

    public JWTUtilImpl(@Value("${spring.jwt.secret}")String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    @Override
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    @Override
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }


    //카테고리 추출
    @Override
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    @Override
    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    @Override
    public String createJwt(String category, String username, String role, Long expiredMs, CurrentTime currentTime) {

        expiredMs = expiredMs * 1000L;

        return Jwts.builder()
                .claim("category",category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(currentTime.getCurrentTime()))
                .expiration(new Date(currentTime.getCurrentTime() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}