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
class MemberFindUsernameDTOTest {


    @Autowired
    private Validator validator;


    @Test
    @DisplayName("조건에 부합하는 requestDTO가 입력되면 에러를 반환하지 않는다")
    public void dto_test(){
        //given
        MemberFindUsernameDTO.MemberFindUsernameRequestDTO dto = MemberFindUsernameDTO.MemberFindUsernameRequestDTO.builder()
                .name("test")
                .phoneNumber("010-1111-1111")
                .build();

        // when
        Set<ConstraintViolation<MemberFindUsernameDTO.MemberFindUsernameRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isEmpty();
    }


    @Test
    @DisplayName("전화번호 형식에 맞지 않는 requestDTO가 입력되면 정해진 에러를 반환한다")
    public void dto_valid_test(){
        //given
        MemberFindUsernameDTO.MemberFindUsernameRequestDTO dto = MemberFindUsernameDTO.MemberFindUsernameRequestDTO.builder()
                .name("test")
                .phoneNumber("error_phone_number")
                .build();

        //when
        Set<ConstraintViolation<MemberFindUsernameDTO.MemberFindUsernameRequestDTO>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("유효하지 않은 전화번호 형식입니다");
    }


    @Test
    @DisplayName("responseDTO 의 username이 형식과 일치하지 않으면 에러를 반환한다")
    public void response_valid_test(){
        //given
        MemberFindUsernameDTO.MemberFindUsernameResponseDTO dto = MemberFindUsernameDTO.MemberFindUsernameResponseDTO.builder()
                .username("err")
                .memberId(1L)
                .build();

        //when
        Set<ConstraintViolation<MemberFindUsernameDTO.MemberFindUsernameResponseDTO>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("Username이 패턴과 일치하지 않습니다");
    }

}