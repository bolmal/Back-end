package com.example.bolmalre.domain.member.web.port;

import com.example.bolmalre.domain.member.domain.Member;
import com.example.bolmalre.domain.member.web.dto.MemberJoinDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface OAuthService {

    // 추후 추가 정보 기입을 위한 로직 필요
    Member kakaoLogin(String accessCode, HttpServletResponse httpServletResponse);

    Member naverLogin(String accessCode, HttpServletResponse httpServletResponse);

    MemberJoinDTO.MemberSocialResponseDTO social(MemberJoinDTO.MemberSocialRequestDTO requestDTO, HttpServletResponse httpServletResponse);
}
