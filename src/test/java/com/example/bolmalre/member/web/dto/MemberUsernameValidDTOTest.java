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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberUsernameValidDTOTest {

    @Autowired
    private Validator validator;


    @Test
    @DisplayName("조건에 부합하는 DTO가 입력되면 오류를 반환하지 않는다")
    public void memberUsernameValidDTOT_success(){
        //given
        MemberUsernameValidDTO.MemberUsernameValidRequestDTO requestDTO = MemberUsernameValidDTO.MemberUsernameValidRequestDTO.builder()
                .username("test123")
                .build();

        // when
        Set<ConstraintViolation<MemberUsernameValidDTO.MemberUsernameValidRequestDTO>> violations = validator.validate(requestDTO);

        // then
        assertThat(violations).isEmpty();
    }


    @Test
    @DisplayName("username 패턴에 일치하지 않으면 정해진 예외를 반환한다")
    public void memberUsernameValidDTOT_pattern(){
        //given
        MemberUsernameValidDTO.MemberUsernameValidRequestDTO requestDTO = MemberUsernameValidDTO.MemberUsernameValidRequestDTO.builder()
                .username("err")
                .build();

        // when
        Set<ConstraintViolation<MemberUsernameValidDTO.MemberUsernameValidRequestDTO>> violations = validator.validate(requestDTO);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("Username이 패턴과 일치하지 않습니다");
    }

}