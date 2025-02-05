package com.example.bolmalre.member.web.controller;

import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.service.OAuthServiceImpl;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Tag(name = "소셜로그인 API")
public class MemberOAuthController {

    private final OAuthServiceImpl authService;

    @Operation(summary = "카카오 인증서버 토큰 검증 API",
    description = "slack에 공유되어 있는 엔드포인트로 POST 요청을 보내주세요 <br><br>" +
            "해당 엔드포인트로 리다이렉트 되어 로직이 작동합니다")
    @GetMapping("/kakao/callback")
    public ApiResponse<?> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {
        Member member = authService.oAuthLogin(accessCode, httpServletResponse);

        return ApiResponse.onSuccess(member.getId());
    }

    @Operation(summary = "프론트 전임 카카오 소셜로그인 API")
    @PostMapping("/kakao/front")
    public ApiResponse<MemberJoinDTO.MemberSocialResponseDTO> social(@RequestBody MemberJoinDTO.MemberSocialRequestDTO request){
        MemberJoinDTO.MemberSocialResponseDTO result = authService.social(request);

        return ApiResponse.onSuccess(result);
    }
}
