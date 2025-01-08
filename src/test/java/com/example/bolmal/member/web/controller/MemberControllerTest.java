package com.example.bolmal.member.web.controller;

import com.example.bolmal.common.apiPayLoad.ApiResponse;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.mock.FakeBCrypt;
import com.example.bolmal.member.mock.FakeMemberService;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberControllerTest {


    private MemberController memberController;

    @BeforeEach
    public void setUp() {

        FakeMemberService fakeMemberService = new FakeMemberService();

        this.memberController = MemberController.builder()
                .memberService(fakeMemberService)
                .build();

    }

    @Test
    public void title(){

        //given
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test")
                .password("test")
                .name("test")
                .nickname("test")
                .phoneNumber("test")
                .birthDate(LocalDate.of(2025, 1, 8))
                .email("test@test.test")
                .gender(Gender.FEMALE)
                .build();

        //when
        ApiResponse<MemberJoinDTO.MemberJoinResponseDTO> result
                = memberController.join(request);

        //then
        assertThat(result.getCode()).isEqualTo("COMMON200");
    }

}