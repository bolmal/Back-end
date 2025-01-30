package com.example.bolmalre.member.web.port;

import com.example.bolmalre.member.web.dto.*;
import jakarta.validation.Valid;

public interface MemberService {
    MemberJoinDTO.MemberJoinResponseDTO joinMember(@Valid MemberJoinDTO.MemberJoinRequestDTO request);

    MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request, String username);

    MemberProfileDTO.MemberProfileResponseDTO get(String username);

    void delete(String username);

    void rollback(String username);

    String resetPassword(String username, MemberUpdateDTO.MemberPasswordUpdateRequestDTO request);

    void validPassword(String username, MemberUpdateDTO.MemberPasswordUpdateRequestDTO request);

    MemberFindUsernameDTO.MemberFindUsernameResponseDTO getUsername(MemberFindUsernameDTO.MemberFindUsernameRequestDTO request);

    MemberFindPasswordDTO.MemberFindPasswordResponseDTO getPassword(MemberFindPasswordDTO.MemberFindPasswordRequestDTO request);

}
