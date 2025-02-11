package com.example.bolmalre.member.service.port;

public interface BCryptHolder {
    String encode(String password);

    boolean matches(String validPassword, String password);
}
