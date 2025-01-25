package com.example.bolmalre.auth.web.controller;


import com.example.bolmalre.auth.domain.Refresh;
import com.example.bolmalre.auth.jwt.JWTUtilImpl;
import com.example.bolmalre.auth.service.port.RefreshRepository;
import com.example.bolmalre.config.JWTConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@ResponseBody
@RequiredArgsConstructor
@Tag(name = "리프레시,액세스 토큰 재발급 API", description = "리프레시,액세스 토큰 재발급 API입니다.")
public class ReissueController {

    private final JWTUtilImpl jwtUtil;
    private final JWTConfig jwtConfig;
    private final RefreshRepository refreshRepository;



    @Operation(
            summary = "리프레시,액세스 토큰 재발급 API",
            description = "리프레시,액세스 토큰 재발급 API입니다.<br>" +
                    "리프레시 토큰 탈취에 대비하여 액세스와 함께 리프레시 토큰도 재발급하는 Refresh Rotate 로직입니다 "
    )
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            throw new IllegalArgumentException("리프레시 토큰이 존재하지 않습니다");
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다");
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            throw new IllegalArgumentException("리프레시 토큰이 존재하지 않습니다");
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response body
            throw new IllegalArgumentException("리프레시 토큰이 존재하지 않습니다");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, jwtConfig.getAccessTokenValidityInSeconds());
        String newRefresh = jwtUtil.createJwt("refresh", username, role, jwtConfig.getRefreshTokenValidityInSeconds());

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username, newRefresh, jwtConfig.getRefreshTokenValidityInSeconds());

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }


    //쿠키 생성
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);


        Refresh refreshToken = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshToken);
    }
}