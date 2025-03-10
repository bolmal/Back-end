package com.example.bolmalre.member.web.controller;

import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.MemberProfileImage;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.service.MemberServiceImpl;
import com.example.bolmalre.member.service.port.BCryptHolder;
import com.example.bolmalre.member.service.port.LocalDateHolder;
import com.example.bolmalre.member.service.port.MemberProfileImageRepository;
import com.example.bolmalre.member.service.port.MemberRepository;
import com.example.bolmalre.member.web.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class MemberControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    MemberProfileImageRepository memberProfileImageRepository;

    @Mock
    MemberServiceImpl memberService;

    @Mock
    LocalDateHolder localDateHolder;

    @SpyBean
    BCryptHolder bCryptHolder;

    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("test12")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .status(Status.ACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .memberProfileImages(new ArrayList<>())
                .build();

        MemberProfileImage testMemberProfileImage = MemberProfileImage.builder()
                .imageLink("testImageLink")
                .fileName("testFileName")
                .imageName("testImageName")
                .member(member)
                .build();

        member.getMemberProfileImages().add(testMemberProfileImage);

        memberRepository.save(member);
        memberProfileImageRepository.save(testMemberProfileImage);
    }


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
    @DisplayName("username이 중복되면 정해진 예외를 반환한다")
    public void join_fail_username_duplicate() throws Exception {
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test12")
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.username").value("중복된 username입니다"));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.password").value("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다"));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.username").value("Username이 패턴과 일치하지 않습니다"));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.email").value("must be a well-formed email address"));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4002"))
                .andExpect(jsonPath("$.message").value("필수 약관에는 모두 동의를 해주셔야 합니다."));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.phoneNumber").value("유효하지 않은 전화번호 형식입니다"));
    }


    @Test
    @DisplayName("update()를 이용하여 회원정보를 업데이트 할 수 있다")
    @WithMockUser(username = "test12", roles = "USER")
    void update_success() throws Exception {
        // given
        MemberUpdateDTO.MemberUpdateRequestDTO request = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("update123")
                .name("update")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("update@example.com")
                .phoneNumber("010-9999-9999")
                .build();

        MemberUpdateDTO.MemberUpdateResponseDTO response = MemberUpdateDTO.MemberUpdateResponseDTO.builder()
                .memberId(1L)
                        .build();

        given(memberService.update(any(MemberUpdateDTO.MemberUpdateRequestDTO.class), anyString()))
                .willReturn(response);

        // when & then
        mockMvc.perform(patch("/members/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."));
    }


    @Test
    @DisplayName("존재하지 않는 회원의 업데이트를 시도하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test123", roles = "USER")
    public void update_fail_MemberNotFound() throws Exception {
        //given
        MemberUpdateDTO.MemberUpdateRequestDTO request = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("update123")
                .name("update")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("update@example.com")
                .phoneNumber("010-9999-9999")
                .build();

        MemberUpdateDTO.MemberUpdateResponseDTO response = MemberUpdateDTO.MemberUpdateResponseDTO.builder()
                .memberId(1L)
                .build();

        given(memberService.update(any(MemberUpdateDTO.MemberUpdateRequestDTO.class), anyString()))
                .willReturn(response);

        // when & then
        mockMvc.perform(patch("/members/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4004"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
    }


    @Test
    @DisplayName("updateDTO의 username 패턴이 일치하지 않으면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void update_fail_username() throws Exception {
        //given
        MemberUpdateDTO.MemberUpdateRequestDTO request = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("err")
                .name("update")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("update@example.com")
                .phoneNumber("010-9999-9999")
                .build();

        MemberUpdateDTO.MemberUpdateResponseDTO response = MemberUpdateDTO.MemberUpdateResponseDTO.builder()
                .memberId(1L)
                .build();

        given(memberService.update(any(MemberUpdateDTO.MemberUpdateRequestDTO.class), anyString()))
                .willReturn(response);

        // when & then
        mockMvc.perform(patch("/members/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.username").value("Username이 패턴과 일치하지 않습니다"));
    }


    @Test
    @DisplayName("이미 존재하는 username으로 업데이트를 시도하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void update_fail_username_duplicate() throws Exception {
        //given
        Member member = Member.builder()
                .username("existmember")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .status(Status.ACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        memberRepository.save(member);

        MemberUpdateDTO.MemberUpdateRequestDTO request = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("existmember")
                .name("update")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("update@example.com")
                .phoneNumber("010-9999-9999")
                .build();

        MemberUpdateDTO.MemberUpdateResponseDTO response = MemberUpdateDTO.MemberUpdateResponseDTO.builder()
                .memberId(1L)
                .build();

        given(memberService.update(any(MemberUpdateDTO.MemberUpdateRequestDTO.class), anyString()))
                .willReturn(response);

        // when & then
        mockMvc.perform(patch("/members/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4001"))
                .andExpect(jsonPath("$.message").value("중복된 username입니다"));
    }


  @Test
  @DisplayName("email 패턴과 일치하지 않으면 정해진 예외를 반환한다")
  @WithMockUser(username = "test12", roles = "USER")
  public void update_fail_email() throws Exception {
      //given
      MemberUpdateDTO.MemberUpdateRequestDTO request = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
              .username("test123")
              .name("update")
              .gender(Gender.MALE)
              .birthDate(LocalDate.of(1995, 5, 20))
              .email("err")
              .phoneNumber("010-9999-9999")
              .build();

      MemberUpdateDTO.MemberUpdateResponseDTO response = MemberUpdateDTO.MemberUpdateResponseDTO.builder()
              .memberId(1L)
              .build();

      given(memberService.update(any(MemberUpdateDTO.MemberUpdateRequestDTO.class), anyString()))
              .willReturn(response);

      // when & then
      mockMvc.perform(patch("/members/")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andDo(print())
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.isSuccess").value(false))
              .andExpect(jsonPath("$.code").value("COMMON400"))
              .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
              .andExpect(jsonPath("$.result.email").value("must be a well-formed email address"));
  }


  @Test
  @DisplayName("전화번호 패턴과 일치하지 않으면 정해진 예외를 반환한다")
  public void update_fail_phoneNumber() throws Exception {
      //given
      MemberUpdateDTO.MemberUpdateRequestDTO request = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
              .username("test123")
              .name("update")
              .gender(Gender.MALE)
              .birthDate(LocalDate.of(1995, 5, 20))
              .email("err")
              .phoneNumber("err")
              .build();

      MemberUpdateDTO.MemberUpdateResponseDTO response = MemberUpdateDTO.MemberUpdateResponseDTO.builder()
              .memberId(1L)
              .build();

      given(memberService.update(any(MemberUpdateDTO.MemberUpdateRequestDTO.class), anyString()))
              .willReturn(response);

      // when & then
      mockMvc.perform(patch("/members/")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andDo(print())
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.isSuccess").value(false))
              .andExpect(jsonPath("$.code").value("COMMON400"))
              .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
              .andExpect(jsonPath("$.result.phoneNumber").value("유효하지 않은 전화번호 형식입니다"));
  }


  @Test
  @DisplayName("get()을 이용하여 자신의 회원정보를 조회 할 수 있다")
  @WithMockUser(username = "test12", roles = "USER")
  public void get_success() throws Exception {
      // given

      // when & then
      mockMvc.perform(get("/members/")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString("test12")))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.isSuccess").value(true))
              .andExpect(jsonPath("$.code").value("COMMON200"))
              .andExpect(jsonPath("$.message").value("성공입니다."))
              .andExpect(jsonPath("$.result.username").value("test12"))
              .andExpect(jsonPath("$.result.name").value("test"))
              .andExpect(jsonPath("$.result.gender").value("MALE"))
              .andExpect(jsonPath("$.result.birthDate").value("1995-05-20"))
              .andExpect(jsonPath("$.result.email").value("test@example.com"))
              .andExpect(jsonPath("$.result.phoneNumber").value("010-1234-5678"))
              .andExpect(jsonPath("$.result.imagePath").value("testImageLink"));
  }


  @Test
  @DisplayName("존재하지 않는 회원을 조회하면 정해진 예외를 반환한다")
  @WithMockUser(username = "err", roles = "USER")
  public void get_fail_username() throws Exception {
      // given

      // when & then
      mockMvc.perform(get("/members/")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString("test12")))
              .andDo(print())
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.isSuccess").value(false))
              .andExpect(jsonPath("$.code").value("MEMBER4004"))
              .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
  }


  @Test
  @DisplayName("delete()를 이용해서 회원을 비활성화 상태로 전환 할 수 있다")
  @WithMockUser(username = "test12", roles = "USER")
  public void delete_success() throws Exception {
      //given

      // when & then
      mockMvc.perform(patch("/members/delete")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString("test12")))
              .andDo(print())
              .andExpect(jsonPath("$.isSuccess").value(true))
              .andExpect(jsonPath("$.code").value("COMMON200"))
              .andExpect(jsonPath("$.message").value("성공입니다."));

      assertThat(member.getStatus()).isEqualTo(Status.INACTIVE);
  }


  @Test
  @DisplayName("존재하지 않는 회원을 삭제하면 정해진 예외를 반환한다")
  @WithMockUser(username = "err", roles = "USER")
  public void delete_memberNotFound() throws Exception {
      //given

      // when & then
      mockMvc.perform(patch("/members/delete")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString("err")))
              .andDo(print())
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.isSuccess").value(false))
              .andExpect(jsonPath("$.code").value("MEMBER4004"))
              .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
  }


  @Test
  @DisplayName("이미 비활성 상태인 회원을 삭제하면 정해진 예외를 반환한다")
  @WithMockUser(username = "test12", roles = "USER")
  public void delete_already_deleted() throws Exception {
      //given
      Member.delete(member, localDateHolder);

      // when & then
      mockMvc.perform(patch("/members/delete")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString("test12")))
              .andDo(print())
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.isSuccess").value(false))
              .andExpect(jsonPath("$.code").value("MEMBER4007"))
              .andExpect(jsonPath("$.message").value("회원이 이미 비활성 상태입니다"));

      assertThat(member.getStatus()).isEqualTo(Status.INACTIVE);
  }


    @Test
    @DisplayName("rollback()을 이용하여 회원을 활성상태로 전환 할 수 있다")
    @WithMockUser(username = "test12", roles = "USER")
    public void rollback_success() throws Exception {
        //given
        Member.delete(member, localDateHolder);

        // when & then
        mockMvc.perform(patch("/members/rollback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("test12")))
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."));

        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    }


    @Test
    @DisplayName("존재하지 않는 회원을 rollback 하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void rollback_fail_already_ACTIVE() throws Exception {
        //given

        // when & then
        mockMvc.perform(patch("/members/rollback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("test12")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4008"))
                .andExpect(jsonPath("$.message").value("회원이 이미 활성 상태입니다"));

        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    }


    @Test
    @DisplayName("존재하지 않는 회원을 rollback하면 정해진 예외를 반환한다")
    @WithMockUser(username = "err", roles = "USER")
    public void rollback_fail_memberNotFound() throws Exception {
        //given

        // when & then
        mockMvc.perform(patch("/members/rollback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("err")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4004"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
    }


    @Test
    @DisplayName("changePassword()를 이용해서 회원의 비밀번호를 재설정 할 수 있다")
    @WithMockUser(username = "test12", roles = "USER")
    public void changePassword_success() throws Exception {
        //given
        MemberUpdateDTO.MemberPasswordUpdateRequestDTO request = MemberUpdateDTO.MemberPasswordUpdateRequestDTO.builder()
                .newPassword("NewPass12!")
                .build();

        when(bCryptHolder.encode(anyString())).thenReturn("encodedPassword");

        // when & then
        mockMvc.perform(patch("/members/profiles/passwords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."));

        assertThat(member.getPassword()).isEqualTo("encodedPassword");
        verify(bCryptHolder, Mockito.times(1)).encode(anyString());
    }


    @Test
    @DisplayName("패턴에 맞지 않는 비밀번호로 교체를 시도할 시, 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void changePassword_fail_password() throws Exception {
        //given
        MemberUpdateDTO.MemberPasswordUpdateRequestDTO request = MemberUpdateDTO.MemberPasswordUpdateRequestDTO.builder()
                .newPassword("err")
                .build();

        // when & then
        mockMvc.perform(patch("/members/profiles/passwords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.newPassword").value("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다"));

        verify(bCryptHolder, Mockito.times(0)).encode(anyString());
    }


    @Test
    @DisplayName("존재하지 않는 회원이 비밀번호 변경을 요청 할 시 정해진 예외를 반환한다")
    @WithMockUser(username = "err", roles = "USER")
    public void changePassword_fail_memberNotFound() throws Exception {
        //given
        MemberUpdateDTO.MemberPasswordUpdateRequestDTO request = MemberUpdateDTO.MemberPasswordUpdateRequestDTO.builder()
                .newPassword("newPass12!")
                .build();

        // when & then
        mockMvc.perform(patch("/members/profiles/passwords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4004"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
    }


    // BCrypt의 경우 숨어 있는 의존성으로서 추후 인터페이스화 해야합니다. 일단은 호출횟수로서 검증하고 따로 브랜치를 생성하여 수정하겠습니다 FIXME
    @Test
    @DisplayName("validPassoword를 이용해서 회원의 비밀번호를 검증 할 수 있다")
    @WithMockUser(username = "test12", roles = "USER")
    public void validPassword_success() throws Exception {
        //given
        MemberPasswordValidDTO.MemberPasswordValidRequestDTO request = MemberPasswordValidDTO.MemberPasswordValidRequestDTO.builder()
                .validPassword("Test123!")
                .build();

        when(bCryptHolder.matches(anyString(),anyString())).thenReturn(true);

        // when & then
        mockMvc.perform(patch("/members/profiles/passwords/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."));

        verify(bCryptHolder, Mockito.times(1)).matches(anyString(),anyString());
    }


    @Test
    @DisplayName("존재하지 않는 회원의 비밀번호를 검증하면 정해진 예외를 반환한다")
    @WithMockUser(username = "err", roles = "USER")
    public void validPassword_fail_memberNotFound() throws Exception {
        //given
        MemberPasswordValidDTO.MemberPasswordValidRequestDTO request = MemberPasswordValidDTO.MemberPasswordValidRequestDTO.builder()
                .validPassword("Test123!")
                .build();

        // when & then
        mockMvc.perform(patch("/members/profiles/passwords/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4004"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
    }


    @Test
    @DisplayName("비밀번호 패턴에 맞지 않는 비밀번호로 업데이트를 시도하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void validPassword_fail_password() throws Exception {
        //given
        MemberPasswordValidDTO.MemberPasswordValidRequestDTO request = MemberPasswordValidDTO.MemberPasswordValidRequestDTO.builder()
                .validPassword("err")
                .build();

        // when & then
        mockMvc.perform(patch("/members/profiles/passwords/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.validPassword").value("비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다"));
    }


    @Test
    @DisplayName("getUsername을 이용해서 아이디를 조회 할 수 있다")
    public void getUsername_success() throws Exception {
        //given

        // when & then
        // GET 메서드는 바디로 요청을 받는게 안된다는걸 깜빡하고 삽질을 ....
        mockMvc.perform(get("/members/usernames")
                        .param("name", "test")
                        .param("phoneNumber", "010-1234-5678"))
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result.username").value("test12"));
    }


    @Test
    @DisplayName("회원을 찾지 못하면 정해진 예외를 반환한다")
    public void getUsername_fail() throws Exception {
        //given

        // when & then
        mockMvc.perform(get("/members/usernames")
                        .param("name", "err")
                        .param("phoneNumber", "010-1234-5678"))
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4004"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
    }


    @Test
    @DisplayName("핸드폰 번호가 정해진 패턴과 일치하지 않으면 정해진 예외를 반환한다")
    public void getUsername_fail_phoneNumber() throws Exception {
        //given

        // when & then
        mockMvc.perform(get("/members/usernames")
                        .param("name", "test")
                        .param("phoneNumber", "err"))
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.result.phoneNumber").value("유효하지 않은 전화번호 형식입니다"));
    }


    // 비밀번호 재설정 API에 대한 테스트 코드 역시 리팩터링 후 구현하겠습니다 FIXME

    @Test
    @DisplayName("validUsernames를 이용하여 Username의 중복검사를 진행 할 수 있다" +
            "중복이 아니면 true를 반환한다")
    public void validUsernames_success() throws Exception {
        //given
        MemberUsernameValidDTO.MemberUsernameValidRequestDTO request = MemberUsernameValidDTO.MemberUsernameValidRequestDTO.builder()
                .username("newusername")
                .build();

        // when & then
        mockMvc.perform(post("/members/valid/usernames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").value(true));
    }


    @Test
    @DisplayName("username이 중복되면 false를 반환한다")
    public void validUsernames_false() throws Exception {
        //given
        MemberUsernameValidDTO.MemberUsernameValidRequestDTO request = MemberUsernameValidDTO.MemberUsernameValidRequestDTO.builder()
                .username("test12")
                .build();

        // when & then
        mockMvc.perform(post("/members/valid/usernames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").value(false));
    }
}