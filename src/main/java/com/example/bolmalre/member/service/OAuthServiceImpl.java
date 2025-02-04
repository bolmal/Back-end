package com.example.bolmalre.member.service;

import com.example.bolmalre.auth.jwt.JWTUtilImpl;
import com.example.bolmalre.auth.service.RefreshTokenService;
import com.example.bolmalre.config.JWTConfig;
import com.example.bolmalre.member.converter.MemberConverter;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import com.example.bolmalre.member.infrastructure.UuidHolder;
import com.example.bolmalre.member.util.KakaoUtil;
import com.example.bolmalre.member.web.dto.KakaoDTO;
import com.example.bolmalre.member.web.port.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.bolmalre.member.util.CookieUtil.createCookie;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final KakaoUtil kakaoUtil;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    private final JWTUtilImpl jwtUtil;
    private final JWTConfig jwtConfig;

    private final UuidHolder uuid;

    // 추후 추가 정보 기입을 위한 로직 필요
    @Override
    public Member oAuthLogin(String accessCode, HttpServletResponse httpServletResponse) {

        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        String requestEmail = kakaoProfile.getKakao_account().getEmail();

        Member byEmail = memberRepository.findByEmail(requestEmail)
                .orElseGet(() -> createNewUser(kakaoProfile));

        loginProcess(httpServletResponse, byEmail);

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

    private void loginProcess(HttpServletResponse httpServletResponse, Member byEmail) {
        String username = byEmail.getUsername();
        Role role = byEmail.getRole();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role.toString(), jwtConfig.getAccessTokenValidityInSeconds());// 1시간
        String refresh = jwtUtil.createJwt("refresh", username, role.toString(), jwtConfig.getRefreshTokenValidityInSeconds());//30일

        //응답 설정
        httpServletResponse.setHeader("access", access);
        httpServletResponse.addCookie(createCookie("refresh", refresh));

        refreshTokenService.addRefreshEntity(username,refresh, jwtConfig.getRefreshTokenValidityInSeconds());
    }
}