package com.example.bolmal.member.mock;

import com.example.bolmal.member.util.BCrypt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FakeBCrypt implements BCrypt {

    private final String mockStr = "test";

    @Override
    public String encode(String rawPassword) {
        return rawPassword + mockStr;
    }


    @Test
    @DisplayName("BCryptPasswordEncoder를 추상화하여 비밀번호를 인코딩 할 수 있다")
    public void title(){
        //given
        String s = "test";

        //when
        String result = encode("test");

        //then
        Assertions.assertThat(result).isEqualTo("testtest");
    }
}
