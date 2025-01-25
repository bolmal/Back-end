package com.example.bolmalre.member;

import jakarta.validation.Valid;

public interface MemberService {
    MemberJoinDTO.MemberJoinResponseDTO joinMember(@Valid MemberJoinDTO.MemberJoinRequestDTO request);
}
