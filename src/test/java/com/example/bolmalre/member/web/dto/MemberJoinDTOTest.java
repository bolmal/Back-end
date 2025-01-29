package com.example.bolmalre.member.web.dto;

import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.web.port.MemberService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class MemberJoinDTOTest {


    @Autowired
    private Validator validator;


    @Test
    @DisplayName("유효한 DTO가 정상적으로 검증되는지 테스트")
    void validDTOTest() {
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
        assertThat(violations).isEmpty(); // 유효한 데이터이므로 검증 오류가 없어야 함
    }

}