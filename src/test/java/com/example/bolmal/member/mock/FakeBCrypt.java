package com.example.bolmal.member.mock;

import com.example.bolmal.member.util.BCrypt;

public class FakeBCrypt implements BCrypt {

    private final String mockStr = "test";

    @Override
    public String encode(String rawPassword) {
        return rawPassword + mockStr;
    }
}
