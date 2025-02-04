package com.example.bolmalre.member.oauth;

import com.example.bolmalre.auth.jwt.JWTUtilImpl;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtil kakaoUtil;
    private final MemberRepository memberRepository;
    private final JWTUtilImpl jwtUtil;
    private final PasswordEncoder passwordEncoder;


    public void oAuthLogin(String accessCode, HttpServletResponse httpServletResponse) {

        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        String requestEmail = kakaoProfile.getKakao_account().getEmail();

        Member byEmail = memberRepository.findByEmail(requestEmail)
                .orElseGet(() -> createNewUser(kakaoProfile));

    }

    private Member createNewUser(KakaoDTO.KakaoProfile kakaoProfile) {
        Member newUser = AuthConverter.toUser(
                kakaoProfile.getKakao_account().getEmail(),
                kakaoProfile.getKakao_account().getProfile().getNickname(),
                null,
                passwordEncoder
        );
        return userRepository.save(newUser);
    }
}