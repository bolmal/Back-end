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
class MemberUpdateDTOTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("조건에 부합하는 DTO가 입력되면 오류를 반환하지 않는다")
    public void DTO_valid(){
        //given
        MemberUpdateDTO.MemberUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("test123")
                .name("update")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("update@example.com")
                .phoneNumber("010-9999-9999")
                .build();

        // when
        Set<ConstraintViolation<MemberUpdateDTO.MemberUpdateRequestDTO>> violations = validator.validate(updateRequestDTO);

        // then
        assertThat(violations).isEmpty();
    }


    @Test
    @DisplayName("username 형식에 맞지 않으면 정해진 오류를 반환한다")
    public void DTO_username_valid(){
        // given
        MemberUpdateDTO.MemberUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("tt")
                .name("update")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("update@example.com")
                .phoneNumber("010-9999-9999")
                .build();

        // when
        Set<ConstraintViolation<MemberUpdateDTO.MemberUpdateRequestDTO>> violations = validator.validate(updateRequestDTO);

        // then
        assertThat(violations).isNotEmpty();  // 검증 실패 시 violations은 비어 있지 않아야 함
        assertThat(violations).extracting("message").contains("Username이 패턴과 일치하지 않습니다");  // 검증 메시지 확인

    }


    @Test
    @DisplayName("Email 형식에 부합하지 않는 DTO가 입력되면 오류를 반환한다")
    public void title(){
        //given
        MemberUpdateDTO.MemberUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("test123")
                .name("update")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("update")
                .phoneNumber("010-9999-9999")
                .build();

        // when
        Set<ConstraintViolation<MemberUpdateDTO.MemberUpdateRequestDTO>> violations = validator.validate(updateRequestDTO);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("올바른 형식의 이메일 주소여야 합니다");
    }


    @Test
    @DisplayName("전화번호 형식에 맞지 않으면 정해진 오류를 반환한다")
    public void DTO_phoneNumber_valid(){
        MemberUpdateDTO.MemberUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("test123")
                .name("update")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("update")
                .phoneNumber("0100000")
                .build();

        // when
        Set<ConstraintViolation<MemberUpdateDTO.MemberUpdateRequestDTO>> violations = validator.validate(updateRequestDTO);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("유효하지 않은 전화번호 형식입니다");
    }


    @Test
    @DisplayName("업데이트 시, 비밀번호 형식에 맞지 않으면 정해진 오류를 반환한다")
    public void DTO_password_valid(){
        //given
        MemberUpdateDTO.MemberPasswordUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberPasswordUpdateRequestDTO.builder()
                .newPassword("updatedPassword")
                .build();

        //when
        Set<ConstraintViolation<MemberUpdateDTO.MemberPasswordUpdateRequestDTO>> violations = validator.validate(updateRequestDTO);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다");
    }

}