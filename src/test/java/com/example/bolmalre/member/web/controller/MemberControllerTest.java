package com.example.bolmalre.member.web.controller;

import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MemberControllerTest {


    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;


    @Test
    @DisplayName("join()을 통해 회원가입을 진행 할 수 있다")
    void join_success() throws Exception {
        // given
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123!")
                .name("test")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1, 1, 1))
                .email("test@naver.com")
                .phoneNumber("010-1111-1111")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(true)
                .build();

        // when & then
        mockMvc.perform(post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result.memberId").exists());
    }


    @Test
    @DisplayName("비밀번호 패턴에 불일치 할 시, 정해진 예외를 반환한다")
    void join_fail_password() throws Exception {
        // given
        String ERROR_PASSWORD = "error_password";

        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password(ERROR_PASSWORD)
                .name("test")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1, 1, 1))
                .email("test@naver.com")
                .phoneNumber("010-1111-1111")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(true)
                .build();

        // when & then
        mockMvc.perform(post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("username 패턴에 불일치 할 시 정해진 예외를 반환한다")
    public void join_fail_username() throws Exception {
        // given
        String ERROR_USERNAME = "error_username";

        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username(ERROR_USERNAME)
                .password("Test123!")
                .name("test")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1, 1, 1))
                .email("test@naver.com")
                .phoneNumber("010-1111-1111")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(true)
                .build();

        // when & then
        mockMvc.perform(post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("이메일 패턴에 불일치 할 시 정해진 예외를 반환한다")
    void join_fail_email() throws Exception {
        // given
        String ERROR_EMAIL = "error_email";

        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123!")
                .name("test")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1, 1, 1))
                .email(ERROR_EMAIL)
                .phoneNumber("010-1111-1111")
                .serviceAgreement(true)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(true)
                .build();

        // when & then
        mockMvc.perform(post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("필수 약관에 모두 동의하지 않으면 정해진 예외를 반환한다")
    public void join_fail_agreement() throws Exception {
        //given
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123!")
                .name("test")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1, 1, 1))
                .email("test@naver.com")
                .phoneNumber("010-1111-1111")
                .serviceAgreement(false)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(true)
                .build();

        // when & then
        mockMvc.perform(post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("전화번호 패턴에 일치하지 않으면 정해진 예외를 반환한다")
    public void title() throws Exception {
        // given
        String ERROR_PHONE = "error_phone";

        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123!")
                .name("test")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1, 1, 1))
                .email("test@naver.com")
                .phoneNumber(ERROR_PHONE)
                .serviceAgreement(false)
                .privacyAgreement(true)
                .financialAgreement(true)
                .advAgreement(true)
                .build();

        // when & then
        mockMvc.perform(post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}