package com.example.bolmalre.member.web.port;

import com.example.bolmalre.member.domain.Member;
import jakarta.servlet.http.HttpServletResponse;

public interface OAuthService {
    Member oAuthLogin(String accessCode, HttpServletResponse httpServletResponse);
}
