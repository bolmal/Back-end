package com.example.bolmalre.auth.filter;


import com.example.bolmalre.auth.jwt.JWTUtilImpl;
import com.example.bolmalre.auth.service.RefreshTokenService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.config.JWTConfig;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.service.port.MemberRepository;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;
    private final JWTUtilImpl jwtUtil;
    private final JWTConfig jwtConfig;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper 추가
    private final RefreshTokenService refreshTokenService;

    private final MemberRepository memberRepository;



    //login을 시작하는 메서드 -> UsernamePasswordAuthenticationFilter 에서 상속 받음
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("로그인 시도");
        try {
            // JSON 요청 본문을 LoginRequestDto로 매핑
            MemberJoinDTO.MemberJoinRequestDTO loginRequestDto = objectMapper.readValue(request.getInputStream(), MemberJoinDTO.MemberJoinRequestDTO.class);

            String username = loginRequestDto.getUsername();
            String password = loginRequestDto.getPassword();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // 인증 시도
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            log.info("로그인 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        log.info("로그인 성공");

        // 유저 정보 가져오기
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, jwtConfig.getAccessTokenValidityInSeconds()); // 1시간
        String refresh = jwtUtil.createJwt("refresh", username, role, jwtConfig.getRefreshTokenValidityInSeconds()); // 30일

        // 응답 헤더 및 쿠키 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));

        refreshTokenService.addRefreshEntity(username, refresh, jwtConfig.getRefreshTokenValidityInSeconds());

        // 유저 정보 조회
        Member byUsername = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member.setLogin(byUsername);
        memberRepository.save(byUsername);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        var responseBody = new LinkedHashMap<String, Object>();

        responseBody.put("isSuccess", true);
        responseBody.put("code", "COMMON200");
        responseBody.put("message", "성공입니다");

        var result = new LinkedHashMap<String, Object>();
        result.put("isLogin", byUsername.isLogin());
        result.put("alarmCount", byUsername.getAlarmAccount());
        result.put("upComming", "test");
        result.put("imagePath", "test_image");
        result.put("name", byUsername.getName());
        result.put("bookmarkCount", byUsername.getBookmarkAccount());
        result.put("isSubscribe", byUsername.getStatus());
        result.put("memberId", byUsername.getId());

        responseBody.put("result", result);

        // JSON 변환 후 응답
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));

        response.setStatus(HttpStatus.OK.value());
    }



    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        log.info("로그인 실패");

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 상태 코드 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 순서를 유지하기 위해 LinkedHashMap 사용
        var responseBody = new LinkedHashMap<String, Object>();

        responseBody.put("isSuccess", false);
        responseBody.put("code", "MEMBER4010");
        responseBody.put("message", "로그인에 실패하였습니다");
        responseBody.put("result", new LinkedHashMap<>()); // 빈 객체 유지

        // JSON 변환 후 응답
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }





    //쿠키 생성
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60*60*60);
        cookie.setSecure(false);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }


}