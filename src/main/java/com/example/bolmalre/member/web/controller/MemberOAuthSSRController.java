package com.example.bolmalre.member.web.controller;

import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.service.OAuthServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth/ssr")
@Tag(name = "소셜로그인 SSR")
public class MemberOAuthSSRController {

    private final OAuthServiceImpl authService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("kakaoClientId", kakaoClientId);
        model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);
        model.addAttribute("naverClientId", naverClientId);
        model.addAttribute("naverRedirectUri", naverRedirectUri);
        return "oauth/login"; // Thymeleaf 템플릿 경로
    }

    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam("code") String accessCode,
                             HttpServletResponse httpServletResponse,
                             Model model) {

        Member member = authService.kakaoLogin(accessCode, httpServletResponse);
        model.addAttribute("member", member);
        return "oauth/success";
    }

    @GetMapping("/naver/callback")
    public String naverLogin(@RequestParam("code") String accessCode,
                             HttpServletResponse httpServletResponse,
                             Model model) {
        Member member = authService.naverLogin(accessCode, httpServletResponse);
        model.addAttribute("member", member);
        return "oauth/success";
    }
}
