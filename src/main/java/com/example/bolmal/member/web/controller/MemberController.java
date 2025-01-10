package com.example.bolmal.member.web.controller;

import com.example.bolmal.common.apiPayLoad.ApiResponse;
import com.example.bolmal.member.web.port.MemberService;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<MemberJoinDTO.MemberJoinResponseDTO> join(@RequestBody MemberJoinDTO.MemberJoinRequestDTO request) {

        MemberJoinDTO.MemberJoinResponseDTO response = memberService.joinMember(request);

        return ApiResponse.onSuccess(response);
    }


    /**

        회원정보 수정

        1. 프로필 사진
        2. 이름
        3. 성별
        4. 이메일
        5. 휴대폰 번호
            5-1. 휴대폰 인증 필요 (카카오톡 인증번호)
        6. 아이디
        7. 비밀번호

     * */



    /**

        회원정보 조회

        1. 프로필 사진
        2. 이름
        3. 성별
        4. 이메일
        5. 휴대폰 번호
        6. 아이디
        7. 비밀번호

     * */



    /**

        회원정보 삭제

        SOFT-DELETE: 얼마나 기다릴지 정해야함

     * */



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
