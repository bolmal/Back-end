package com.example.bolmalre.member.web.dto;

import com.example.bolmalre.member.domain.enums.Gender;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MemberJoinDTOTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("조건에 부합하는 DTO가 입력되면 오류를 반환하지 않는다")
    void DTO_Valid() {
        // given
        MemberJoinDTO.MemberJoinRequestDTO dto = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123!")
                .name("test")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .phoneNumber("010-1234-5678")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(false)
                .build();

        // when
        Set<ConstraintViolation<MemberJoinDTO.MemberJoinRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("username 형식에 맞지 않으면 정해진 오류를 반환한다")
    public void DTO_username_valid(){
        // given
        MemberJoinDTO.MemberJoinRequestDTO dto = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("tt")
                .password("Test123!")
                .name("test")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .phoneNumber("010-1234-5678")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(false)
                .build();

        // when
        Set<ConstraintViolation<MemberJoinDTO.MemberJoinRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();  // 검증 실패 시 violations은 비어 있지 않아야 함
        assertThat(violations).extracting("message").contains("Username이 패턴과 일치하지 않습니다");  // 검증 메시지 확인

    }

    @Test
    @DisplayName("비밀번호 형식에 맞지 않으면 정해진 오류를 반환한다")
    public void DTO_password_valid(){
        //given
        MemberJoinDTO.MemberJoinRequestDTO dto = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123")
                .name("test")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .phoneNumber("010-1234-5678")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(false)
                .build();

        // when
        Set<ConstraintViolation<MemberJoinDTO.MemberJoinRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();  // 검증 실패 시 violations은 비어 있지 않아야 함
        assertThat(violations).extracting("message").contains("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다");  // 검증 메시지 확인
    }

    @Test
    @DisplayName("이메일 형식에 맞지 않으면 정해진 오류를 반환다")
    public void DTO_email_valid(){
        //given
        MemberJoinDTO.MemberJoinRequestDTO dto = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123!")
                .name("test")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("test")
                .phoneNumber("010-1234-5678")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(false)
                .build();

        // when
        Set<ConstraintViolation<MemberJoinDTO.MemberJoinRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("올바른 형식의 이메일 주소여야 합니다");
    }

    @Test
    @DisplayName("전화번호 형식에 맞지 않으면 정해진 오류를 반환한다")
    public void DTO_phoneNumber_valid(){
        // given
        MemberJoinDTO.MemberJoinRequestDTO dto = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123!")
                .name("test")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .phoneNumber("test")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(false)
                .build();

        // when
        Set<ConstraintViolation<MemberJoinDTO.MemberJoinRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("유효하지 않은 전화번호 형식입니다");
    }

}