package com.example.bolmalre.member.service;

import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Agreement;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.MemberProfileImage;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.infrastructure.AgreementRepository;
import com.example.bolmalre.member.infrastructure.LocalDateHolder;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import com.example.bolmalre.member.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    AgreementRepository agreementRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    LocalDateHolder localDateHolder;

    @InjectMocks
    MemberServiceImpl memberService;

    MemberProfileImage memberProfileImage;

    Member testMember;

    @BeforeEach
    void setUp() {

        testMember = Member.builder()
                .id(1L)
                .username("test123")
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

        memberProfileImage = MemberProfileImage.builder()
                .imageLink("test")
                .imageName("test")
                .fileName("test")
                .member(testMember)
                .build();

        testMember.setMemberProfileImages(List.of(memberProfileImage));
    }

    @Test
    @DisplayName("joinMember() 메서드를 이용하여 회원가입을 할 수 있다")
    public void joinMember_test(){
        // given
        MemberJoinDTO.MemberJoinRequestDTO requestDTO = MemberJoinDTO.MemberJoinRequestDTO.builder()
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

        Member mockMember = Member.builder()
                .id(1L)
                .username("testUser")
                .password("encodedPassword")
                .build();

        Agreement mockAgreement = Agreement.builder()
                .id(1L)
                .member(mockMember)
                .serviceAgreement(true)
                .build();

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(memberRepository.save(Mockito.any(Member.class))).thenReturn(mockMember);
        when(agreementRepository.save(Mockito.any(Agreement.class))).thenReturn(mockAgreement);

        // when
        MemberJoinDTO.MemberJoinResponseDTO responseDTO = memberService.joinMember(requestDTO);

        // then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMemberId()).isEqualTo(mockMember.getId());

        // Verify
        verify(memberRepository, Mockito.times(1)).save(Mockito.any(Member.class));
    }


    @Test
    @DisplayName("회원가입 시 BCrypt로 비밀번호가 해싱된다")
    public void joinMember_withPasswordHashing_test() {
        // given
        MemberJoinDTO.MemberJoinRequestDTO requestDTO = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")
                .password("Test123!")  // 평문 비밀번호
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

        // mock 객체 준비
        Member mockMember = Member.builder()
                .id(1L)
                .username("testUser")
                .password("encodedPassword")  // 해싱된 비밀번호
                .build();

        Agreement mockAgreement = Agreement.builder()
                .id(1L)
                .member(mockMember)
                .serviceAgreement(true)
                .build();

        String encodedPassword = "encodedPassword";  // 해싱된 비밀번호
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(memberRepository.save(Mockito.any(Member.class))).thenReturn(mockMember);
        when(agreementRepository.save(Mockito.any(Agreement.class))).thenReturn(mockAgreement);

        // when
        MemberJoinDTO.MemberJoinResponseDTO responseDTO = memberService.joinMember(requestDTO);

        // then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMemberId()).isEqualTo(mockMember.getId());

        assertThat(mockMember.getPassword()).isEqualTo(encodedPassword);

        verify(bCryptPasswordEncoder, Mockito.times(1)).encode(anyString());
        verify(memberRepository, Mockito.times(1)).save(Mockito.any(Member.class));
    }



    @Test
    @DisplayName("이미 존재하는 username으로 회원가입을 요청하면 예외가 발생해야 한다")
    void joinMember_duplicateUsername_test() {
        // given
        MemberJoinDTO.MemberJoinRequestDTO requestDTO = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")  // 이미 존재하는 유저네임
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

        when(memberRepository.existsByUsername("test123")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.joinMember(requestDTO))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_USERNAME_DUPLICATE);

        verify(memberRepository, Mockito.never()).save(Mockito.any(Member.class));
    }


    @Test
    @DisplayName("필수약관에 동의하지 않으면 회원가입 시 오류를 반환한다")
    public void joinMember_Agreement_test(){
        //given
        MemberJoinDTO.MemberJoinRequestDTO requestDTO = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("test123")  // 이미 존재하는 유저네임
                .password("Test123!")
                .name("test")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .phoneNumber("010-1234-5678")
                .serviceAgreement(false)
                .privacyAgreement(false)
                .financialAgreement(false)
                .advAgreement(false)
                .build();

        // when & then
        assertThatThrownBy(() -> memberService.joinMember(requestDTO))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_AGREEMENT);

        verify(memberRepository, Mockito.never()).save(Mockito.any(Member.class));
    }


    @Test
    @DisplayName("update() 메서드를 이용해서 회원정보를 업데이트할 수 있다")
    public void update_test() {
        // given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        when(memberRepository.save(Mockito.any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MemberUpdateDTO.MemberUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("update123") // username 변경
                .name("update") // 변경
                .gender(Gender.MALE) // 변경 없음
                .birthDate(LocalDate.of(1995, 5, 20)) // 변경 없음
                .email("update@example.com") // 변경
                .phoneNumber("010-9999-9999") // 변경
                .build();

        // when
        assert member != null;
        MemberUpdateDTO.MemberUpdateResponseDTO response = memberService.update(updateRequestDTO, member.getUsername());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMemberId()).isEqualTo(member.getId());

        assertThat(member.getName()).isEqualTo(updateRequestDTO.getName());
        assertThat(member.getEmail()).isEqualTo(updateRequestDTO.getEmail());
        assertThat(member.getPhoneNumber()).isEqualTo(updateRequestDTO.getPhoneNumber());

        verify(memberRepository, Mockito.times(1)).save(member);
    }


    @Test
    @DisplayName("업데이트 로직 실행 시, username 중복을 검증 할 수 있다")
    public void update_username_valid(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        Member duplicateMember = Member.builder()
                .id(1L)
                .username("testtest123!")
                .password("testtest123")
                .name("testtest123")
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

        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        when(memberRepository.existsByUsername(duplicateMember.getUsername())).thenReturn(true);

        MemberUpdateDTO.MemberUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("testtest123!") // username 변경
                .name("update") // 변경
                .gender(Gender.MALE) // 변경 없음
                .birthDate(LocalDate.of(1995, 5, 20)) // 변경 없음
                .email("update@example.com") // 변경
                .phoneNumber("010-9999-9999") // 변경
                .build();

        //when
        assert member != null;
        assertThatThrownBy(() -> memberService.update(updateRequestDTO,member.getUsername()))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_USERNAME_DUPLICATE);

        //then
        verify(memberRepository, Mockito.times(0)).save(member);
    }


    @Test
    @DisplayName("업데이트 로직 실행 시, username이 변하지 않더라도 중복오류가 발생하지 않는다")
    public void update_username_invalid(){
        // given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        when(memberRepository.save(Mockito.any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MemberUpdateDTO.MemberUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("test123") // 같은 username이지만, 기존의 username이므로 에러가 반환되면 안됨
                .name("update") // 변경
                .gender(Gender.MALE) // 변경 없음
                .birthDate(LocalDate.of(1995, 5, 20)) // 변경 없음
                .email("update@example.com") // 변경
                .phoneNumber("010-9999-9999") // 변경
                .build();

        // when
        assert member != null;
        MemberUpdateDTO.MemberUpdateResponseDTO response = memberService.update(updateRequestDTO, member.getUsername());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMemberId()).isEqualTo(member.getId());

        assertThat(member.getName()).isEqualTo(updateRequestDTO.getName());
        assertThat(member.getEmail()).isEqualTo(updateRequestDTO.getEmail());
        assertThat(member.getPhoneNumber()).isEqualTo(updateRequestDTO.getPhoneNumber());

        verify(memberRepository, Mockito.times(1)).save(member);
    }


    @Test
    @DisplayName("get() 메서드를 이용해서 회원정보를 조회 할 수 있다")
    public void get_test(){
        //given

        //when
        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(testMember));

        //then
        assert testMember != null;
        MemberProfileDTO.MemberProfileResponseDTO response = memberService.get("test123");

        assertThat(response).isNotNull();

        assertThat(testMember.getUsername()).isEqualTo(response.getUsername());
        assertThat(testMember.getName()).isEqualTo(response.getName());
        assertThat(testMember.getGender()).isEqualTo(response.getGender());
        assertThat(testMember.getBirthday()).isEqualTo(response.getBirthDate());
        assertThat(testMember.getEmail()).isEqualTo(response.getEmail());
        assertThat(testMember.getPhoneNumber()).isEqualTo(response.getPhoneNumber());
    }


    @Test
    @DisplayName("존재하지 않는 회원을 조회하면 정해진 에러를 반환한다")
    public void get_valid_test(){
        //given
        String ERROR_USERNAME = "error123";

        //when
        when(memberRepository.findByUsername(ERROR_USERNAME)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> memberService.get(ERROR_USERNAME))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("delete() 메서드를 통해 회원을 비활성화 상태로 전환 할 수 있다")
    public void delete_test(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        //when
        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        when(localDateHolder.now()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        memberService.delete("test123");

        //then
        assert member != null;
        assertThat(member.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(member.getInactiveDate()).isEqualTo(LocalDateTime.of(1, 1, 1, 1, 1));
    }


    @Test
    @DisplayName("존재하지 않는 회원을 삭제하면 정해진 에러를 반환한다")
    public void delete_valid_test(){
        //given
        String ERROR_USERNAME = "error123";

        //when
        when(memberRepository.findByUsername(ERROR_USERNAME)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> memberService.delete(ERROR_USERNAME))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("이미 비활성화 상태인 회원을 삭제하면 정해진 에러를 반환한다")
    public void delete_status_invalid_test(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .status(Status.INACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        //when
        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));

        //then
        assertThatThrownBy(() -> memberService.delete("test123"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_ALREADY_INACTIVE);
    }


    @Test
    @DisplayName("rollback() 메서드를 통해 회원을 활성화 상태로 전환 할 수 있다")
    public void rollback_test(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        //when
        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        when(localDateHolder.now()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        memberService.delete("test123");
        memberService.rollback("test123");

        //then
        assert member != null;
        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(member.getInactiveDate()).isEqualTo(LocalDateTime.of(1, 1, 1, 1, 1));
    }


    @Test
    @DisplayName("존재하지 않는 회원을 복구하면 정해진 에러를 반환한다")
    public void rollback_valid_test(){
        //given
        String ERROR_USERNAME = "error123";

        //when
        when(memberRepository.findByUsername(ERROR_USERNAME)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> memberService.rollback(ERROR_USERNAME))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("이미 활성화 상태인 회원을 삭제하면 정해진 에러를 반환한다")
    public void rollback_status_invalid_test(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        //when
        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));

        //then
        assertThatThrownBy(() -> memberService.rollback("test123"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_ALREADY_ACTIVE);
    }

    @Test
    @DisplayName("resetPassword() 를 통해서 마이페이지에서 비밀번호를 재설정 할 수 있다")
    public void resetPassword_test(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("updatedPassword");
        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));

        MemberUpdateDTO.MemberPasswordUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberPasswordUpdateRequestDTO.builder()
                .newPassword("updatedPassword")
                .build();

        //when
        String response = memberService.resetPassword("test123", updateRequestDTO);

        //then
        assertThat(response).isEqualTo("updatedPassword");
    }


    @Test
    @DisplayName("존재하지 않는 회원이 비밀번호를 재설정하면 정해진 오류를 반환한다")
    public void resetPassword_valid_test(){
        //given
        String ERROR_USERNAME = "error123";

        MemberUpdateDTO.MemberPasswordUpdateRequestDTO updateRequestDTO = MemberUpdateDTO.MemberPasswordUpdateRequestDTO.builder()
                .newPassword("updatedPassword")
                .build();

        //when
        when(memberRepository.findByUsername(ERROR_USERNAME)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> memberService.resetPassword(ERROR_USERNAME, updateRequestDTO))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("validPassword() 를 이용하여 회원의 비밀번호를 검증 할 수 있다")
    public void validPassword_test(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        MemberPasswordValidDTO.MemberPasswordValidRequestDTO updateRequestDTO = MemberPasswordValidDTO.MemberPasswordValidRequestDTO.builder()
                .validPassword("Test123!")
                .build();

        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

        //when
        memberService.validPassword("test123", updateRequestDTO);

        //then -> 반환값이 void 이므로 아무일도 일어나지 않아야함
    }


    @Test
    @DisplayName("존재하지 않는 회원의 비밀번호 검증을 시도하면 정해진 에러를 반환한다")
    public void validPassword_valid_test(){
        //given
        String ERROR_USERNAME = "error123";

        MemberPasswordValidDTO.MemberPasswordValidRequestDTO updateRequestDTO = MemberPasswordValidDTO.MemberPasswordValidRequestDTO.builder()
                .validPassword("Test123!")
                .build();

        //when
        when(memberRepository.findByUsername(ERROR_USERNAME)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> memberService.validPassword(ERROR_USERNAME, updateRequestDTO))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("비밀번호 검증에 실패 할 시, 정해진 오류를 반환한다")
    public void title(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        MemberPasswordValidDTO.MemberPasswordValidRequestDTO updateRequestDTO = MemberPasswordValidDTO.MemberPasswordValidRequestDTO.builder()
                .validPassword("not_valid_password")
                .build();

        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> memberService.validPassword("test123", updateRequestDTO))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_PASSWORD_VALID);
    }

    @Test
    @DisplayName("getUsername() 을 이용하여 회원의 username 을 조회 할 수 있다")
    public void getUsername_test(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        when(memberRepository.findByNameAndPhoneNumber(anyString(), anyString())).thenReturn(member);

        MemberFindUsernameDTO.MemberFindUsernameRequestDTO dto = MemberFindUsernameDTO.MemberFindUsernameRequestDTO.builder()
                .name("test")
                .phoneNumber("010-1234-5678")
                .build();

        //when
        MemberFindUsernameDTO.MemberFindUsernameResponseDTO response = memberService.getUsername(dto);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getMemberId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("test123");
    }


    @Test
    @DisplayName("존재하지 않는 회원의 username 조회 시, 정해진 오류를 반환한다")
    public void getUsername_valid_test(){
        //given
        when(memberRepository.findByNameAndPhoneNumber(anyString(), anyString()))
                .thenReturn(null);

        MemberFindUsernameDTO.MemberFindUsernameRequestDTO dto = MemberFindUsernameDTO.MemberFindUsernameRequestDTO.builder()
                .name("error_name")
                .phoneNumber("010-1234-5678")
                .build();

        //when & then
        assertThatThrownBy(() -> memberService.getUsername(dto))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("회원의 username은 일치하지만, 전화번호를 잘못 입력한 경우 정해진 오류를 반환한다")
    public void getUsername_valid_phoneNumber_test(){
        //given
        when(memberRepository.findByNameAndPhoneNumber(anyString(), anyString()))
                .thenReturn(null);

        MemberFindUsernameDTO.MemberFindUsernameRequestDTO dto = MemberFindUsernameDTO.MemberFindUsernameRequestDTO.builder()
                .name("error_name")
                .phoneNumber("error_phoneNumber")
                .build();

        //when & then
        assertThatThrownBy(() -> memberService.getUsername(dto))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("getPassword() 를 통해 회원의 비밀번호를 초기화 할 수 있다")
    public void getPassword_test(){
        //given
        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        MemberFindPasswordDTO.MemberFindPasswordRequestDTO dto = MemberFindPasswordDTO.MemberFindPasswordRequestDTO.builder()
                .username("test123")
                .newPassword("updatedPassword")
                .name("test")
                .phoneNumber("010-1234-5678")
                .build();

        when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        when(memberRepository.findByNameAndPhoneNumber("test","010-1234-5678")).thenReturn(member);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encoded_password");

        //when
        MemberFindPasswordDTO.MemberFindPasswordResponseDTO response = memberService.getPassword(dto);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getMemberId()).isEqualTo(1L);
        assertThat(response.getNewPassword()).isEqualTo("updatedPassword");
    }


    @Test
    @DisplayName("존재하지 않는 username을 통해 비밀번호를 초기화하면 오류를 반환한다")
    public void getPassword_valid_test(){
        //given
        String ERROR_USERNAME = "error123";

        MemberFindPasswordDTO.MemberFindPasswordRequestDTO dto = MemberFindPasswordDTO.MemberFindPasswordRequestDTO.builder()
                .username(ERROR_USERNAME)
                .newPassword("updatedPassword")
                .name("test")
                .phoneNumber("010-1234-5678")
                .build();

        when(memberRepository.findByUsername(ERROR_USERNAME)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> memberService.getPassword(dto))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("존재하는 username의 전화번호, 이름을 잘못 입력하면 정해진 오류를 반환한다")
    public void getPassword_valid_dto_test(){
        //given

        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        MemberFindPasswordDTO.MemberFindPasswordRequestDTO dto = MemberFindPasswordDTO.MemberFindPasswordRequestDTO.builder()
                .username("test123")
                .newPassword("updatedPassword")
                .name("error")
                .phoneNumber("errorPhoneNumber")
                .build();

        when(memberRepository.findByUsername("test123")).thenReturn(Optional.of(member));
        when(memberRepository.findByNameAndPhoneNumber(anyString(),anyString())).thenReturn(null);

        //when & then
        assertThatThrownBy(() -> memberService.getPassword(dto))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("usernameValid() 를 통해 username이 중복인지 확인 할 수 있다")
    public void usernameValid_success(){
        //given
        MemberUsernameValidDTO.MemberUsernameValidRequestDTO requestDTO = MemberUsernameValidDTO.MemberUsernameValidRequestDTO.builder()
                .username("test1234")
                .build();

        when(memberRepository.findByUsername("test1234")).thenReturn(Optional.empty());

        //when
        boolean result = memberService.usernameValid(requestDTO);

        //then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("username 검증에 실패하면 정해진 예외를 반환한다")
    public void usernameValid_fail(){
        //given

        Member member = Member.builder()
                .id(1L)
                .username("test123")
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

        MemberUsernameValidDTO.MemberUsernameValidRequestDTO requestDTO = MemberUsernameValidDTO.MemberUsernameValidRequestDTO.builder()
                .username("test123")
                .build();

        when(memberRepository.findByUsername("test123")).thenReturn(Optional.of(member));

        //when
        boolean result = memberService.usernameValid(requestDTO);

        //then
        assertThat(result).isFalse();
    }
}