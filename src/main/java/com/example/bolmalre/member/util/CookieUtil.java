package com.example.bolmalre.member.util;

import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public static jakarta.servlet.http.Cookie createCookie(String key, String value) {

        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie(key, value);
        cookie.setMaxAge(24*60*60*60*60);
        cookie.setSecure(false);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
