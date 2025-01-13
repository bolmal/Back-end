package com.example.bolmal.member.web.dto;


import com.example.bolmal.BolmalApplication;
import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.mock.FakeAgreementRepository;
import com.example.bolmal.member.mock.FakeBCrypt;
import com.example.bolmal.member.mock.FakeMemberRepository;
import com.example.bolmal.member.service.MemberServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = BolmalApplication.class)
@ActiveProfiles("test")
public class MemberJoinDTOTest {

    private MemberServiceImpl memberService;

    @Autowired
    private Validator validator;


    @BeforeEach
    void setUp() {

        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakeAgreementRepository fakeAgreementRepository = new FakeAgreementRepository();
        FakeBCrypt fakeBCrypt = new FakeBCrypt();

        this.memberService = MemberServiceImpl.builder()
                .memberRepository(fakeMemberRepository)
                .agreementRepository(fakeAgreementRepository)
                .bCrypt(fakeBCrypt)
                .build();

        fakeMemberRepository.save(
                Member.builder()
                        .id(1L)
                        .username("testtest")
                        .password("Test123!")
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
                        .username("testtest2")
                        .password("Test123!")
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
    @DisplayName("Username 패턴 조건을 만족하지 못하면 오류를 반환한다")
    public void joinMember_username(){
        //given
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("te")
                .password("Test123!")
                .name("test")
                .nickname("test")
                .phoneNumber("test")
                .birthDate(LocalDate.of(2025, 1, 8))
                .email("test@test.test")
                .gender(Gender.FEMALE)
                .advAgreement(Boolean.TRUE)
                .serviceAgreement(Boolean.TRUE)
                .financialAgreement(Boolean.TRUE)
                .privacyAgreement(Boolean.TRUE)
                .build();

        Set<ConstraintViolation<MemberJoinDTO.MemberJoinRequestDTO>> validate = validator.validate(request);

        // then
        assertThat(validate).isNotNull();
        assertThat(validate.iterator().next().getMessage()).contains("Username이 패턴과 일치하지 않습니다");
    }



    @Test
    @DisplayName("Password 조건을 만족하지 못하면 오류를 반환한다")
    public void joinMember_password(){
        //given

        //when

        //then
    }


    @Test
    @DisplayName("Email 조건을 만족하지 못하면 오류를 반환한다")
    public void joinMember_email(){
        //given

        //when

        //then
    }


    @Test
    @DisplayName("필수 약관동의 조건을 만족하지 못하면 오류를 반환한다")
    public void joinMember_agreement(){
        //given

        //when

        //then
    }


    @Test
    @DisplayName("전화번호 인증을 통과하지 못하면 오류를 반환한다")
    public void joinMember_phone_number_validation(){
        //given

        //when

        //then
    }


}
