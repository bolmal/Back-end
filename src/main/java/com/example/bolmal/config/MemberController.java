package com.example.bolmal.config;

import com.example.bolmal.common.apiPayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "개인 정보 관리 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입 API")
    @PostMapping("/join")
    public ApiResponse<MemberJoinDTO.MemberJoinResponseDTO> join(@RequestBody MemberJoinDTO.MemberJoinRequestDTO request) {

        MemberJoinDTO.MemberJoinResponseDTO response = memberService.joinMember(request);

        return ApiResponse.onSuccess(response);
    }
}
