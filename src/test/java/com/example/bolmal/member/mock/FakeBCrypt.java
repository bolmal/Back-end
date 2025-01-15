package com.example.bolmal.member.mock;

import com.example.bolmal.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmal.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmal.member.service.port.BCrypt;
import com.example.bolmal.member.service.port.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class FakeBCrypt implements BCrypt {

    private final String mockStr = "test";

    @Override
    public String encode(String rawPassword) {
        return rawPassword + mockStr;
    }

    @Override
    public boolean matches(String oldPassword, String newPassword) {
        if (oldPassword.equals(encode(newPassword))) {
            return true;
        }
        return false;
    }


    @Test
    @DisplayName("BCryptPasswordEncoder를 추상화하여 비밀번호를 인코딩 할 수 있다")
    public void bcryptPasswordEncoder_encode(){
        //given
        String s = "test";

        //when
        String result = encode("test");

        //then
        Assertions.assertThat(result).isEqualTo("testtest");
    }

    @Test
    @DisplayName("BCryptPasswordEncoder를 추상화하여 두 비밀번호의 일치여부를 판단 할 수 있다")
    public void bcryptPasswordEncoder_matches() {
        String rawPassword = "test";

        //when
        String encodedPassword = encode(rawPassword);
        String inputPassword = "test";

        boolean matches = matches(encodedPassword, inputPassword);

        //then
        assertTrue(matches);
    }
}
