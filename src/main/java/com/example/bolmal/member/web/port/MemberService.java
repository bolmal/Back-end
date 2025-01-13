package com.example.bolmal.member.web.port;

import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.dto.MemberProfileDTO;
import com.example.bolmal.member.web.dto.MemberUpdateDTO;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request);

    MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request,
                                                   String username);

    MemberProfileDTO.MemberProfileResponseDTO get(String username);
}
