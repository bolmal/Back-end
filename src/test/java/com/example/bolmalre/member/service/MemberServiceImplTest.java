package com.example.bolmalre.member.service;

import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Agreement;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.infrastructure.AgreementRepository;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
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

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.MEMBER_AGREEMENT;
import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.MEMBER_USERNAME_DUPLICATE;
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

        // memberRepository.save()가 호출되지 않아야 함
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



}