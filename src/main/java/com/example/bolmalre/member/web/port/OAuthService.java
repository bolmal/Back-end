package com.example.bolmalre.member.web.port;

import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface OAuthService {
    Member oAuthLogin(String accessCode, HttpServletResponse httpServletResponse);

    MemberJoinDTO.MemberSocialResponseDTO social(MemberJoinDTO.MemberSocialRequestDTO requestDTO, HttpServletResponse httpServletResponse);
}
