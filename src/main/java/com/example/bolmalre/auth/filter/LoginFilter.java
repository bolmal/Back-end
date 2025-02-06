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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

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

    //Authentication authentication에서 유저정보를 ㅏㄱ져옴
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        log.info("로그인 성공");

        //유저 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, jwtConfig.getAccessTokenValidityInSeconds());// 1시간
        String refresh = jwtUtil.createJwt("refresh", username, role, jwtConfig.getRefreshTokenValidityInSeconds());//30일

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));

        refreshTokenService.addRefreshEntity(username,refresh, jwtConfig.getRefreshTokenValidityInSeconds());

        response.setStatus(HttpStatus.OK.value());




        Member byUsername = memberRepository.findByUsername(username)
                .orElseThrow(()-> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member.setLogin(byUsername);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        var responseBody = new HashMap<String, Object>();
        responseBody.put("memberId", byUsername.getId());
        responseBody.put("name", byUsername.getName());
        responseBody.put("isLogin", byUsername.isLogin());
        responseBody.put("upComming", "test");
        responseBody.put("alarmCount", byUsername.getAlarmAccount());
        responseBody.put("bookmarkCount", byUsername.getBookmarkAccount());
        responseBody.put("isSubscribe", byUsername.getStatus());
        responseBody.put("imagePath","test_image");

        memberRepository.save(byUsername);

        // JSON 응답 반환
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
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