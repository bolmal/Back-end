package com.example.bolmal.member.service;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.mock.FakeBCrypt;
import com.example.bolmal.member.mock.FakeMemberRepository;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.port.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {

        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakeBCrypt fakeBCrypt = new FakeBCrypt();

        this.memberService = MemberServiceImpl.builder()
                .memberRepository(fakeMemberRepository)
                .bCrypt(fakeBCrypt)
                .build();

        fakeMemberRepository.save(
                Member.builder()
                        .id(1L)
                        .username("test")
                        .password("test")
                        .name("test")
                        .nickname("test")
                        .role(Role.ROLE_USER)
                        .phoneNumber("test")
                        .birthday(LocalDate.of(2025,1,8))
                        .email("test@test.test")
                        .status(Status.ACTIVE)
                        .gender(Gender.FEMALE)
                        .build());

        fakeMemberRepository.save(
                Member.builder()
                        .id(2L)
                        .username("test2")
                        .password("test2")
                        .name("test2")
                        .nickname("test2")
                        .role(Role.ROLE_USER)
                        .phoneNumber("test2")
                        .birthday(LocalDate.of(2025,1,8))
                        .email("test2@test.test")
                        .status(Status.ACTIVE)
                        .gender(Gender.FEMALE)
                        .build());

    }
    
    
    @Test
    @DisplayName("joinMember은 requestDTO를 이용하여 유저를 생성 할 수 있다")
    public void joinMember(){
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
        MemberJoinDTO.MemberJoinResponseDTO result = memberService.joinMember(request);


        //then
        assertThat(result).isNotNull();
        // 앞에서 두개 만들고 해서 3나옴
        assertThat(result.getMemberId()).isEqualTo(3L);
    }


}