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
class MemberPasswordValidDTOTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("조건에 부합하는 DTO가 입력되면 오류를 반환하지 않는다")
    public void MemberPasswordValidDTO_Test(){
        //given
        MemberPasswordValidDTO.MemberPasswordValidRequestDTO updateRequestDTO = MemberPasswordValidDTO.MemberPasswordValidRequestDTO.builder()
                .validPassword("Test123!")
                .build();

        // when
        Set<ConstraintViolation<MemberPasswordValidDTO.MemberPasswordValidRequestDTO>> violations = validator.validate(updateRequestDTO);

        // then
        assertThat(violations).isEmpty();
    }


    @Test
    @DisplayName("비밀번호가 형식에 맞지 않으면 정해진 오류를 반환한다")
    public void title(){
        //given
        MemberPasswordValidDTO.MemberPasswordValidRequestDTO updateRequestDTO = MemberPasswordValidDTO.MemberPasswordValidRequestDTO.builder()
                .validPassword("err")
                .build();

        // when
        Set<ConstraintViolation<MemberPasswordValidDTO.MemberPasswordValidRequestDTO>> violations = validator.validate(updateRequestDTO);

        // then
        assertThat(violations).isNotEmpty();  // 검증 실패 시 violations은 비어 있지 않아야 함
        assertThat(violations).extracting("message").contains("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다");  // 검증 메시지 확인
    }

}