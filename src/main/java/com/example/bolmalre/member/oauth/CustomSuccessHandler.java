package com.example.bolmalre.member.oauth;


import com.example.bolmalre.auth.jwt.JWTUtil;
import com.example.bolmalre.auth.service.RefreshTokenService;
import com.example.bolmalre.config.JWTConfig;
import com.example.bolmalre.member.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;

import static com.example.bolmalre.member.util.CookieUtil.createCookie;


@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RefreshTokenService refreshTokenService;
    @Qualifier("JWTUtil")
    private final JWTUtil jwtUtil;
    private final JWTConfig jwtConfig;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String name=customUserDetails.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        Integer expireS = 24 * 60 * 60;

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role.toString(), jwtConfig.getAccessTokenValidityInSeconds());// 1시간
        String refresh = jwtUtil.createJwt("refresh", username, role.toString(), jwtConfig.getRefreshTokenValidityInSeconds());//30일

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));

        refreshTokenService.addRefreshEntity(username,refresh, jwtConfig.getRefreshTokenValidityInSeconds());

/*        String encodedName = URLEncoder.encode(name, "UTF-8");
        response.sendRedirect("http://localhost:3000/oauth2-jwt-header?name=" + encodedName);*/

    }
}
