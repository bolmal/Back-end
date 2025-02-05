package com.example.bolmalre.member.web.controller;

import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.service.OAuthServiceImpl;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import com.example.bolmalre.member.web.port.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Tag(name = "소셜로그인 API")
@Slf4j
public class MemberOAuthController {


    private final OAuthService authService;


    @Operation(summary = "카카오 인증서버 토큰 검증 API")
    @GetMapping("/kakao/callback")
    public ApiResponse<?> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {

        Member member = authService.kakaoLogin(accessCode, httpServletResponse);

        return ApiResponse.onSuccess(member.getId());
    }


    @Operation(summary = "네이버 인증서버 토큰 검증 API")
    @GetMapping("/naver/callback")
    public ApiResponse<?> naverLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {

        Member member = authService.naverLogin(accessCode, httpServletResponse);

        return ApiResponse.onSuccess(member.getId());
    }


    @Operation(summary = "프론트 전임 카카오 소셜로그인 API")
    @PostMapping("/kakao/front")
    public ApiResponse<MemberJoinDTO.MemberSocialResponseDTO> social(@RequestBody MemberJoinDTO.MemberSocialRequestDTO request,
                                                                     HttpServletResponse httpServletResponse){
        MemberJoinDTO.MemberSocialResponseDTO result = authService.social(request,httpServletResponse);

        return ApiResponse.onSuccess(result);
    }
}
