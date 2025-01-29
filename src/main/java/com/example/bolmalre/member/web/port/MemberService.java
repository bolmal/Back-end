package com.example.bolmalre.member.web.port;

import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import jakarta.validation.Valid;

public interface MemberService {
    MemberJoinDTO.MemberJoinResponseDTO joinMember(@Valid MemberJoinDTO.MemberJoinRequestDTO request);
}
