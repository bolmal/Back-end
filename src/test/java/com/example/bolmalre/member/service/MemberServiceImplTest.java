package com.example.bolmalre.member.service;

import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Agreement;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.infrastructure.AgreementRepository;
import com.example.bolmalre.member.infrastructure.LocalDateHolder;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import com.example.bolmalre.member.web.dto.MemberProfileDTO;
import com.example.bolmalre.member.web.dto.MemberUpdateDTO;
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
import java.util.Optional;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        Mockito.when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenReturn(mockMember);
        Mockito.when(agreementRepository.save(Mockito.any(Agreement.class))).thenReturn(mockAgreement);

        // when
        MemberJoinDTO.MemberJoinResponseDTO responseDTO = memberService.joinMember(requestDTO);

        // then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMemberId()).isEqualTo(mockMember.getId());

        // Verify
        Mockito.verify(memberRepository, Mockito.times(1)).save(Mockito.any(Member.class));
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
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);
        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenReturn(mockMember);
        Mockito.when(agreementRepository.save(Mockito.any(Agreement.class))).thenReturn(mockAgreement);

        // when
        MemberJoinDTO.MemberJoinResponseDTO responseDTO = memberService.joinMember(requestDTO);

        // then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMemberId()).isEqualTo(mockMember.getId());

        assertThat(mockMember.getPassword()).isEqualTo(encodedPassword);

        Mockito.verify(bCryptPasswordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(memberRepository, Mockito.times(1)).save(Mockito.any(Member.class));
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

        Mockito.when(memberRepository.existsByUsername("test123")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.joinMember(requestDTO))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_USERNAME_DUPLICATE);

        Mockito.verify(memberRepository, Mockito.never()).save(Mockito.any(Member.class));
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

        Mockito.verify(memberRepository, Mockito.never()).save(Mockito.any(Member.class));
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
                .profileImage(null)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        Mockito.when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

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

        Mockito.verify(memberRepository, Mockito.times(1)).save(member);
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
                .profileImage(null)
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
                .profileImage(null)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        Mockito.when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        Mockito.when(memberRepository.existsByUsername(duplicateMember.getUsername())).thenReturn(true);

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
        Mockito.verify(memberRepository, Mockito.times(0)).save(member);
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
                .profileImage(null)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        Mockito.when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

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

        Mockito.verify(memberRepository, Mockito.times(1)).save(member);
    }


    @Test
    @DisplayName("get() 메서드를 이용해서 회원정보를 조회 할 수 있다")
    public void get_test(){
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
                .profileImage(null)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        //when
        Mockito.when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));

        //then
        assert member != null;
        MemberProfileDTO.MemberProfileResponseDTO response = memberService.get("test123");

        assertThat(response).isNotNull();

        assertThat(member.getUsername()).isEqualTo(response.getUsername());
        assertThat(member.getName()).isEqualTo(response.getName());
        assertThat(member.getGender()).isEqualTo(response.getGender());
        assertThat(member.getBirthday()).isEqualTo(response.getBirthDate());
        assertThat(member.getEmail()).isEqualTo(response.getEmail());
        assertThat(member.getPhoneNumber()).isEqualTo(response.getPhoneNumber());
        assertThat(member.getProfileImage()).isEqualTo(response.getImagePath());
    }


    @Test
    @DisplayName("존재하지 않는 회원을 조회하면 정해진 에러를 반환한다")
    public void get_valid_test(){
        //given
        String ERROR_USERNAME = "error123";

        //when
        Mockito.when(memberRepository.findByUsername(ERROR_USERNAME)).thenReturn(Optional.empty());

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
                .profileImage(null)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        //when
        Mockito.when(memberRepository.findByUsername("test123")).thenReturn(Optional.ofNullable(member));
        Mockito.when(localDateHolder.now()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        memberService.delete("test123");

        //then
        assert member != null;
        assertThat(member.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(member.getInactiveDate()).isEqualTo(LocalDateTime.of(1, 1, 1, 1, 1));
    }


    @Test
    @DisplayName("rollback() 메서드를 통해 회원을 활성화 상태로 전환 할 수 있다")
    public void rollback_test(){
        //given

        //when

        //then
    }
}