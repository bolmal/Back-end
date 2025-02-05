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
import com.example.bolmalre.member.util.NaverUtil;
import com.example.bolmalre.member.web.dto.KakaoDTO;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import com.example.bolmalre.member.web.dto.NaverDTO;
import com.example.bolmalre.member.web.port.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.bolmalre.member.util.CookieUtil.createCookie;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthServiceImpl implements OAuthService {

    private final KakaoUtil kakaoUtil;
    private final NaverUtil naverUtil;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    private final JWTUtilImpl jwtUtil;
    private final JWTConfig jwtConfig;

    private final UuidHolder uuid;

    // 추후 추가 정보 기입을 위한 로직 필요
    @Override
    public Member kakaoLogin(String accessCode, HttpServletResponse httpServletResponse) {

        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        String requestEmail = kakaoProfile.getKakao_account().getEmail();

        Member byEmail = memberRepository.findByEmail(requestEmail)
                .orElseGet(() -> createNewUser(kakaoProfile));

        loginProcess(httpServletResponse, byEmail);

        return byEmail;
    }

    @Override
    public Member naverLogin(String accessCode, HttpServletResponse httpServletResponse) {

        log.info("토큰이 전달되었습니다: "+accessCode);

        NaverDTO.OAuthToken oAuthToken = naverUtil.requestToken(accessCode);
        NaverDTO.NaverProfile naverProfile = naverUtil.requestProfile(oAuthToken);
        String requestEmail = naverProfile.getNaver_account().getEmail();

        Member byEmail = memberRepository.findByEmail(requestEmail)
                .orElseGet(() -> createNaverNewUser(naverProfile));

        loginProcess(httpServletResponse, byEmail);

        return byEmail;
    }

    private Member createNaverNewUser(NaverDTO.NaverProfile naverProfile) {
        Member newMember = MemberConverter.toOAuthMember(
                naverProfile.getNaver_account().getEmail(),
                naverProfile.getNaver_account().getProfile().getNickname(),
                "naver",
                passwordEncoder,
                uuid
        );
        return memberRepository.save(newMember);
    }



    @Override
    public MemberJoinDTO.MemberSocialResponseDTO social(MemberJoinDTO.MemberSocialRequestDTO requestDTO, HttpServletResponse httpServletResponse) {

        Member byEmail = memberRepository.findByEmail(requestDTO.getEmail())
                .orElse(null);

        if (byEmail == null) {
            byEmail = MemberConverter.toFrontKakaoMember(
                    requestDTO.getName(), requestDTO.getEmail(), "front_social",passwordEncoder,uuid);

            Member newMember = memberRepository.save(byEmail);
            loginProcess(httpServletResponse, newMember);
            return MemberConverter.toMemberSocialResponseDTO(newMember);
        }

        loginProcess(httpServletResponse, byEmail);
        return MemberConverter.toMemberSocialResponseDTO(byEmail);
    }


    private Member createNewUser(KakaoDTO.KakaoProfile kakaoProfile) {
        Member newMember = MemberConverter.toOAuthMember(
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