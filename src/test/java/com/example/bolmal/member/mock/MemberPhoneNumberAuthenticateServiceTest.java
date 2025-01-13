package com.example.bolmal.member.mock;

import com.example.bolmal.member.service.MemberPhoneNumberAuthenticateServiceImpl;
import com.example.bolmal.member.web.port.MemberPhoneNumberAuthenticateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberPhoneNumberAuthenticateServiceTest {

    private MemberPhoneNumberAuthenticateServiceImpl memberPhoneNumberAuthenticateService;

    @BeforeEach
    void setUp() {
        this.memberPhoneNumberAuthenticateService = MemberPhoneNumberAuthenticateServiceImpl.builder()
                .build();
    }

    @Test
    @DisplayName("전화번호 인증을 실패하면 false를 반환한다")
    public void title(){
        //given

        String password = memberPhoneNumberAuthenticateService.getPassword("010-9396-5971");

        //when
        boolean result = memberPhoneNumberAuthenticateService.authenticate(password);

        //then
        assertTrue(result);
    }


}
