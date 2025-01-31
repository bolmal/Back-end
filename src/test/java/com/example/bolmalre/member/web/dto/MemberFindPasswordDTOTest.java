package com.example.bolmalre.member.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MemberFindPasswordDTOTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("조건에 부합하는 DTO가 입력되면 에러를 반환하지 않는다")
    public void dto_test(){
        //given
        MemberFindPasswordDTO.MemberFindPasswordRequestDTO dto = MemberFindPasswordDTO.MemberFindPasswordRequestDTO.builder()
                .username("test123")
                .newPassword("Test123!")
                .name("test")
                .phoneNumber("010-1234-5678")
                .build();

        // when
        Set<ConstraintViolation<MemberFindPasswordDTO.MemberFindPasswordRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isEmpty();
    }


    @Test
    @DisplayName("username 패턴에 일치하지 않는 데이터가 입력되면 정해진 오류를 반환한다")
    public void dto_username_valid_test(){
        //given
        MemberFindPasswordDTO.MemberFindPasswordRequestDTO dto = MemberFindPasswordDTO.MemberFindPasswordRequestDTO.builder()
                .username("err")
                .newPassword("Test123!")
                .name("test")
                .phoneNumber("010-1234-5678")
                .build();

        //when
        Set<ConstraintViolation<MemberFindPasswordDTO.MemberFindPasswordRequestDTO>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("Username이 패턴과 일치하지 않습니다");
    }


    @Test
    @DisplayName("비밀번호 패턴에 일치하지 않는 데이터가 입력되면 정해진 오류를 반환한다")
    public void dto_password_valid_test(){
        //given
        MemberFindPasswordDTO.MemberFindPasswordRequestDTO dto = MemberFindPasswordDTO.MemberFindPasswordRequestDTO.builder()
                .username("test123")
                .newPassword("err")
                .name("test")
                .phoneNumber("010-1234-5678")
                .build();

        //when
        Set<ConstraintViolation<MemberFindPasswordDTO.MemberFindPasswordRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다");
    }


    @Test
    @DisplayName("전화번호 패턴에 일치하지 않는 데이터가 입력되면 정해진 오류를 반환한다")
    public void dto_phoneNumber_valid_test(){
        //given
        MemberFindPasswordDTO.MemberFindPasswordRequestDTO dto = MemberFindPasswordDTO.MemberFindPasswordRequestDTO.builder()
                .username("test123")
                .newPassword("Test123!")
                .name("test")
                .phoneNumber("err")
                .build();

        // when
        Set<ConstraintViolation<MemberFindPasswordDTO.MemberFindPasswordRequestDTO>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("유효하지 않은 전화번호 형식입니다");
    }

}