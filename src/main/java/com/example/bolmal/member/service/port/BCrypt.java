package com.example.bolmal.member.service.port;

public interface BCrypt {
    String encode(String rawPassword);

    boolean matches(String oldPassword, String newPassword);
}
