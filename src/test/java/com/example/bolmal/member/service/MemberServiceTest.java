package com.example.bolmal.member.service;

import com.example.bolmal.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.mock.FakeAgreementRepository;
import com.example.bolmal.member.mock.FakeBCrypt;
import com.example.bolmal.member.mock.FakeLocalDateTimeHolder;
import com.example.bolmal.member.mock.FakeMemberRepository;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.dto.MemberProfileDTO;
import com.example.bolmal.member.web.dto.MemberUpdateDTO;
import org.junit.jupiter.api.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.bolmal.common.apiPayLoad.code.status.ErrorStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MemberServiceTest {

    private MemberServiceImpl memberService;
    FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
    FakeBCrypt fakeBCrypt = new FakeBCrypt();
    FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder();

    @BeforeEach
    void setUp() {

        FakeAgreementRepository fakeAgreementRepository = new FakeAgreementRepository();

        this.memberService = MemberServiceImpl.builder()
                .memberRepository(fakeMemberRepository)
                .agreementRepository(fakeAgreementRepository)
                .bCrypt(fakeBCrypt)
                .localDate(fakeLocalDateTimeHolder)
                .build();

        fakeMemberRepository.save(
                Member.builder()
                        .id(1L)
                        .username("testtest")
                        .password("Test123!")
                        .name("test")
                        .nickname("test")
                        .role(Role.ROLE_USER)
                        .phoneNumber("test")
                        .birthday(LocalDate.of(2025, 1, 8))
                        .email("test@test.test")
                        .status(Status.ACTIVE)
                        .gender(Gender.FEMALE)
                        .build());

        fakeMemberRepository.save(
                Member.builder()
                        .id(2L)
                        .username("testtest2")
                        .password("Test123!")
                        .name("test2")
                        .nickname("test2")
                        .role(Role.ROLE_USER)
                        .phoneNumber("test2")
                        .birthday(LocalDate.of(2025,1,8))
                        .email("test2@test.test")
                        .status(Status.ACTIVE)
                        .gender(Gender.FEMALE)
                        .build());

    }

    @AfterEach
    void tearDown() {
        fakeMemberRepository.clear(); // 데이터를 초기화하는 메서드 호출
    }



    @Test
    @DisplayName("joinMember()은 requestDTO를 이용하여 유저를 생성 할 수 있다")
    public void member_join(){
        //given
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("testtest3")
                .password("Test123!")
                .name("test")
                .nickname("test")
                .phoneNumber("test")
                .birthDate(LocalDate.of(2025, 1, 8))
                .email("test@test.test")
                .gender(Gender.FEMALE)
                .advAgreement(Boolean.TRUE)
                .serviceAgreement(Boolean.TRUE)
                .financialAgreement(Boolean.TRUE)
                .privacyAgreement(Boolean.TRUE)
                .build();


        //when
        MemberJoinDTO.MemberJoinResponseDTO result = memberService.joinMember(request);


        //then
        assertThat(result).isNotNull();
        // 앞에서 두개 만들고 해서 3나옴
        assertThat(result.getMemberId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("회원가입 과정에서 BCrypt를 이용하여 비밀번호를 인코딩 할 수 있다")
    public void joinMember_password(){
        //given
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("testtest3")
                .password("Test123!")
                .name("test")
                .nickname("test")
                .phoneNumber("test")
                .birthDate(LocalDate.of(2025, 1, 8))
                .email("test@test.test")
                .gender(Gender.FEMALE)
                .advAgreement(Boolean.TRUE)
                .serviceAgreement(Boolean.TRUE)
                .financialAgreement(Boolean.TRUE)
                .privacyAgreement(Boolean.TRUE)
                .build();

        //when
        memberService.joinMember(request);
        Member byUsername = fakeMemberRepository.findByUsername("testtest3")
                .orElseThrow();

        String password = byUsername.getPassword();

        //then
        assertThat(password).isEqualTo("Test123!test");
    }

    @Test
    @DisplayName("필수 약관동의 조건을 만족하지 못하면 오류를 반환한다")
    public void joinMember_agreement(){
        //given
        MemberJoinDTO.MemberJoinRequestDTO request = MemberJoinDTO.MemberJoinRequestDTO.builder()
                .username("testtest3")
                .password("Test123!")
                .name("test")
                .nickname("test")
                .phoneNumber("test")
                .birthDate(LocalDate.of(2025, 1, 8))
                .email("test@test.test")
                .gender(Gender.FEMALE)
                .advAgreement(Boolean.TRUE)
                .serviceAgreement(Boolean.TRUE)
                .financialAgreement(Boolean.FALSE) // 필수조건 false
                .privacyAgreement(Boolean.TRUE)
                .build();


        assertThatThrownBy(() -> memberService.joinMember(request))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_AGREEMENT);
    }


    @Test
    @DisplayName("update() 메서드를 이용하여 회원정보를 업데이트 할 수 있다")
    public void member_update(){
        //given
        String username = "testtest";
        String updateUsername = "updatedTest";

        MemberUpdateDTO.MemberUpdateRequestDTO updateMember = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("updatedTest")
                .name("updatedTest")
                .phoneNumber("updatedTest")
                .birthDate(LocalDate.of(2025, 1, 13))
                .email("updatedtest@test.test")
                .gender(Gender.MALE)
                .build();

        //when
        MemberUpdateDTO.MemberUpdateResponseDTO result = memberService.update(updateMember, username);
        Optional<Member> byUsername = fakeMemberRepository.findByUsername(updateUsername);

        //then
        assertThat(result).isNotNull();

        //update 이기에 memberId가 바뀌어선 안된다
        assertThat(result.getMemberId()).isEqualTo(1L);
        assertThat(byUsername.get().getUsername()).isEqualTo("updatedTest");
        assertThat(byUsername.get().getName()).isEqualTo("updatedTest");
    }


    @Test
    @DisplayName("update() 메서드를 이용하여 업데이트한 회원정보를 이전의 정보로 조회 할 수 없다")
    public void member_update_valid(){
        //given
        String username = "testtest";

        MemberUpdateDTO.MemberUpdateRequestDTO updateMember = MemberUpdateDTO.MemberUpdateRequestDTO.builder()
                .username("updatedTest")
                .name("updatedTest")
                .phoneNumber("updatedTest")
                .birthDate(LocalDate.of(2025, 1, 13))
                .email("updatedtest@test.test")
                .gender(Gender.MALE)
                .build();

        memberService.update(updateMember, username);

        //then
        Optional<Member> oldMember = fakeMemberRepository.findByUsername("testtest");
        assertThat(oldMember).isEmpty(); // Optional.empty() -> oldMember가 존재하지 않음
    }


    @Test
    @DisplayName("get() 메서드를 이용하여 회원정보를 조회 할 수 있다")
    public void member_get(){
        //given

        //when
        MemberProfileDTO.MemberProfileResponseDTO result = memberService.get("testtest");

        //then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("test");
        assertThat(result.getEmail()).isEqualTo("test@test.test");

    }

    @Test
    @DisplayName("존재하지 않는 회원은 get() 메서드를 이용하여 조회 할 수 없다")
    public void member_get_valid(){
        //given

        //when

        //then
        assertThatThrownBy(()-> memberService.get("faketest"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);

    }


    @Test
    @DisplayName("delete() 메서드를 이용해서 회원의 상태를 INACTIVE로 전환 할 수 있다" +
            "회원의 삭제 시간을 LocalDateTime을 이용하여 업데이트 할 수 있다")
    public void member_delete(){
        //given

        //when
        memberService.delete("testtest");
        Member byUsername = fakeMemberRepository.findByUsername("testtest")
                .orElseThrow();

        //then
        assertThat(byUsername.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(byUsername.getInactiveDate()).isEqualTo(fakeLocalDateTimeHolder.now());
    }

    @Test
    @DisplayName("deleteOldInactiveMembers() 메서드를 통해서 정해진 일자가 지난 회원을 삭제 할 수 있다")
    public void member_delete_cutOffDays(){
        //given
        LocalDateTime cutOffDays = LocalDateTime.of(2024, 12, 2, 1, 1, 1);

        //when
        memberService.delete("testtest");
        Member byUsername = fakeMemberRepository.findByUsername("testtest")
                .orElseThrow();
        byUsername.setInactiveDate(cutOffDays);

        memberService.deleteOldInactiveMembers(1);

        //then
        assertThat(byUsername.getInactiveDate()).isEqualTo(cutOffDays);
    }

    @Test
    @DisplayName("rollback()을 이용해서 회원을 활성상태로 돌릴 수 있다")
    public void member_rollback(){
        //given
        memberService.delete("testtest");
        Member byUsername = fakeMemberRepository.findByUsername("testtest")
                .orElseThrow();

        //when
        memberService.rollback("testtest");

        //then
        assertThat(byUsername.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("활성 상태인 회원을 rollback() 할 수 없다")
    public void member_rollback_valid(){
        //given

        //when

        //then
        assertThatThrownBy(()->memberService.rollback("testtest"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code",MEMBER_NOT_INACTIVE);
    }

    @Test
    @DisplayName("resetPassword()를 이용하여 비밀번호를 초기화 할 수 있다")
    public void member_password() {
        //given
        MemberUpdateDTO.MemberPasswordUpdateRequestDTO newPassword = MemberUpdateDTO.MemberPasswordUpdateRequestDTO.builder()
                .newPassword("TestTTTT123!")
                .build();

        //when
        memberService.resetPassword("testtest", newPassword);
        Member byUsername = fakeMemberRepository.findByUsername("testtest")
                .orElseThrow();

        String password = byUsername.getPassword();

        //then
        assertThat(fakeBCrypt.encode(newPassword.getNewPassword())).isEqualTo(password);

    }


}