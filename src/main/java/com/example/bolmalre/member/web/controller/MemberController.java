package com.example.bolmalre.member.web.controller;

import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.member.web.dto.*;
import com.example.bolmalre.member.web.port.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "개인 정보 관리 API")
@Builder
public class MemberController {

    private final MemberService memberService;

    // 기획서대로 수정 필요함 FIXME
    @Operation(summary = "회원가입 API")
    @PostMapping("/join")
    public ApiResponse<MemberJoinDTO.MemberJoinResponseDTO> join(@Valid @RequestBody MemberJoinDTO.MemberJoinRequestDTO request) {

        MemberJoinDTO.MemberJoinResponseDTO response = memberService.joinMember(request);

        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "회원정보 업데이트 API")
    @PatchMapping("/")
    public ApiResponse<MemberUpdateDTO.MemberUpdateResponseDTO> update(@Valid @RequestBody MemberUpdateDTO.MemberUpdateRequestDTO request,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {

        MemberUpdateDTO.MemberUpdateResponseDTO result = memberService.update(request, userDetails.getUsername());

        return ApiResponse.onSuccess(result);
    }


    @Operation(summary = "회원정보 조회 API")
    @GetMapping("/")
    public ApiResponse<MemberProfileDTO.MemberProfileResponseDTO> get(@AuthenticationPrincipal UserDetails userDetails) {

        MemberProfileDTO.MemberProfileResponseDTO result = memberService.get(userDetails.getUsername());

        return ApiResponse.onSuccess(result);
    }


    @Operation(summary = "회원삭제 API")
    @PatchMapping("/delete")
    public ApiResponse<String> delete(@AuthenticationPrincipal UserDetails userDetails) {

        memberService.delete(userDetails.getUsername());

        return ApiResponse.onSuccess("정상적으로 삭제되었습니다");
    }


    @Operation(summary = "비활성화 회원, 활성으로 전환 API")
    @PatchMapping("/rollback")
    public ApiResponse<String> rollback(@AuthenticationPrincipal UserDetails userDetails) {
        memberService.rollback(userDetails.getUsername());

        return ApiResponse.onSuccess("정상적으로 복구되었습니다");
    }


    @Operation(summary = "마이프로필 비밀번호 재설정 API")
    @PatchMapping("/profiles/passwords")
    public ApiResponse<String> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                              @Valid @RequestBody MemberUpdateDTO.MemberPasswordUpdateRequestDTO request) {

        String newPasswords = memberService.resetPassword(userDetails.getUsername(), request);

        return ApiResponse.onSuccess(newPasswords);
    }


    @Operation(summary = "마이프로필 비밀번호 검증 API")
    @PatchMapping("/profiles/passwords/valid")
    public ApiResponse<String> validPassword(@AuthenticationPrincipal UserDetails userDetails,
                                             @Valid @RequestBody MemberUpdateDTO.MemberPasswordValidRequestDTO request) {
        memberService.validPassword(userDetails.getUsername(), request);
        return ApiResponse.onSuccess("비밀번호가 정상적으로 검증 되었습니다");
    }


    @Operation(summary = "아이디 찾기 API")
    @GetMapping("/usernames")
    public ApiResponse<MemberFindUsernameDTO.MemberFindUsernameResponseDTO> getUsername(
            @Valid @ModelAttribute MemberFindUsernameDTO.MemberFindUsernameRequestDTO request
    ){

        MemberFindUsernameDTO.MemberFindUsernameResponseDTO result = memberService.getUsername(request);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "비밀번호 찾기(재설정) API")
    @PatchMapping("/passwords")
    public ApiResponse<MemberFindPasswordDTO.MemberFindPasswordResponseDTO> getUsername(
            @Valid @RequestBody MemberFindPasswordDTO.MemberFindPasswordRequestDTO request
    ){

        MemberFindPasswordDTO.MemberFindPasswordResponseDTO result = memberService.getPassword(request);
        return ApiResponse.onSuccess(result);
    }
}
