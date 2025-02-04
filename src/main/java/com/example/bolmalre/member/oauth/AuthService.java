package com.example.bolmalre.member.oauth;

import com.example.bolmalre.auth.filter.LoginFilter;
import com.example.bolmalre.auth.jwt.JWTUtilImpl;
import com.example.bolmalre.auth.service.RefreshTokenService;
import com.example.bolmalre.config.JWTConfig;
import com.example.bolmalre.member.converter.MemberConverter;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import com.example.bolmalre.member.infrastructure.UuidHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import static com.example.bolmalre.member.util.CookieUtil.createCookie;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoUtil kakaoUtil;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    private final JWTUtilImpl jwtUtil;
    private final JWTConfig jwtConfig;

    private final UuidHolder uuid;


    public Member oAuthLogin(String accessCode, HttpServletResponse httpServletResponse) {

        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        String requestEmail = kakaoProfile.getKakao_account().getEmail();

        Member byEmail = memberRepository.findByEmail(requestEmail)
                .orElseGet(() -> createNewUser(kakaoProfile));

        String username = byEmail.getUsername();
        Role role = byEmail.getRole();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role.toString(), jwtConfig.getAccessTokenValidityInSeconds());// 1시간
        String refresh = jwtUtil.createJwt("refresh", username, role.toString(), jwtConfig.getRefreshTokenValidityInSeconds());//30일

        //응답 설정
        httpServletResponse.setHeader("access", access);
        httpServletResponse.addCookie(createCookie("refresh", refresh));

        refreshTokenService.addRefreshEntity(username,refresh, jwtConfig.getRefreshTokenValidityInSeconds());

        return byEmail;
    }

    private Member createNewUser(KakaoDTO.KakaoProfile kakaoProfile) {
        Member newMember = MemberConverter.toKakaoMember(
                kakaoProfile.getKakao_account().getEmail(),
                kakaoProfile.getKakao_account().getProfile().getNickname(),
                "kakao",
                passwordEncoder,
                uuid
        );
        return memberRepository.save(newMember);
    }
}