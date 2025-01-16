package com.example.bolmal.member.web.dto;


import com.example.bolmal.BolmalApplication;
import com.example.bolmal.mail.service.MailServiceImpl;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = BolmalApplication.class)
@ActiveProfiles("test")
public class MemberJoinDTOTest {


    @Autowired
    private Validator validator;

    @MockBean
    private MailServiceImpl mailService;


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
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("testtest")
                .password("Test")
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
        assertThat(validate.iterator().next().getMessage()).contains("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다");
    }


    @Test
    @DisplayName("Email 조건을 만족하지 못하면 오류를 반환한다")
    public void joinMember_email(){
        //given
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("testtest")
                .password("Test123!")
                .name("test")
                .nickname("test")
                .phoneNumber("test")
                .birthDate(LocalDate.of(2025, 1, 8))
                .email("test")
                .gender(Gender.FEMALE)
                .advAgreement(Boolean.TRUE)
                .serviceAgreement(Boolean.TRUE)
                .financialAgreement(Boolean.TRUE)
                .privacyAgreement(Boolean.TRUE)
                .build();

        Set<ConstraintViolation<MemberJoinDTO.MemberJoinRequestDTO>> validate = validator.validate(request);

        // then
        assertThat(validate).isNotNull();
        assertThat(validate.iterator().next().getMessage()).contains("올바른 형식의 이메일 주소여야 합니다");
    }

    @Test
    @DisplayName("PasswordUpdateRequestDTO는 Password 조건을 만족하지 못하면 오류를 반환한다")
    public void member_password_pattern_valid(){
        //given
        MemberUpdateDTO.MemberPasswordUpdateRequestDTO newPassword = MemberUpdateDTO.MemberPasswordUpdateRequestDTO.builder()
                .newPassword("Te")
                .build();

        //when
        Set<ConstraintViolation<MemberUpdateDTO.MemberPasswordUpdateRequestDTO>> validate = validator.validate(newPassword);

        // then
        assertThat(validate).isNotNull();
        assertThat(validate.iterator().next().getMessage()).contains("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다");
    }


}
