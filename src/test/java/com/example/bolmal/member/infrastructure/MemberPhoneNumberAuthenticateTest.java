package com.example.bolmal.member.infrastructure;

import com.example.bolmal.member.service.MemberPhoneNumberAuthenticateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberPhoneNumberAuthenticateTest {

    private MemberPhoneNumberAuthenticateServiceImpl memberPhoneNumberAuthenticate;

    @BeforeEach
    void setUp() {
        this.memberPhoneNumberAuthenticate= MemberPhoneNumberAuthenticateServiceImpl.builder()
                .build();
    }

    @Test
    @DisplayName("전화번호 인증을 통과하지 못하면 오류를 반환한다")
    public void joinMember_phone_number_validation(){
        //given
        String phoneNumber = "123456789";

        String requestPassword = memberPhoneNumberAuthenticate.getPassword(phoneNumber);

        //when
        boolean authenticate = memberPhoneNumberAuthenticate.authenticate(requestPassword);

        //then
        assertTrue(authenticate);
    }

}