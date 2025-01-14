package com.example.bolmal.member.web.controller;

import com.example.bolmal.common.apiPayLoad.ApiResponse;
import com.example.bolmal.member.web.dto.MemberProfileDTO;
import com.example.bolmal.member.web.dto.MemberUpdateDTO;
import com.example.bolmal.member.web.port.MemberService;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
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


    @Operation(summary = "비활성화 회원, 활성으로 돌리기")
    @PatchMapping("/rollback")
    public ApiResponse<String> rollback(@AuthenticationPrincipal UserDetails userDetails) {
        memberService.rollback(userDetails.getUsername());

        return ApiResponse.onSuccess("정상적으로 복구되었습니다");
    }


    /**

        회원정보 찾기 - 아아디

        1. 이름
        2. 핸드폰 번호

        -> 아아디 반환

     * */




    /**

        회원정보 찾기 - 비밀번호 재설정

        1. 아이디 존재여부 확인하고
        2. 이름
        3. 휴대폰 번호 인증하고
        4. 비밀번호 재설정

     * */


}
