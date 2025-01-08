package com.example.bolmal.member.web.port;

import com.example.bolmal.member.web.dto.MemberJoinDTO;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request);

}
