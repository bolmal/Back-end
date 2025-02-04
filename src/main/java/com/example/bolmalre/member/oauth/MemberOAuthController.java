package com.example.bolmalre.member.oauth;

import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.member.domain.Member;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MemberOAuthController {

    private final AuthService authService;

    @GetMapping("/callback")
    public ApiResponse<?> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {
        authService.oAuthLogin(accessCode, httpServletResponse);

        return ApiResponse.onSuccess("성공");
        /*return BaseResponse.onSuccess(UserConverter.toJoinResultDTO(user));*/
    }
}
