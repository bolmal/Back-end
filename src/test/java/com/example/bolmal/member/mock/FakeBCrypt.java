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
        String newPasswords = encode(newPassword);

        if (oldPassword.equals(newPasswords)) {
            throw new MemberHandler(ErrorStatus.MEMBER_PASSWORD_DUPLICATE);
        }
        return true;
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
}
