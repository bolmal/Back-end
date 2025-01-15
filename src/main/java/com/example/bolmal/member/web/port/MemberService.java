package com.example.bolmal.member.web.port;

import com.example.bolmal.member.service.port.LocalDateTimeHolder;
import com.example.bolmal.member.web.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface MemberService {

    MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request);

    MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request,
                                                   String username);

    MemberProfileDTO.MemberProfileResponseDTO get(String username);

    void delete(String username);

    void rollback(String username);

    String resetPassword(String username, MemberUpdateDTO.MemberPasswordUpdateRequestDTO request);

    boolean validPassword(String username, MemberUpdateDTO.MemberPasswordUpdateRequestDTO request);

    MemberFindUsernameDTO.MemberFindUsernameResponseDTO getUsername(MemberFindUsernameDTO.MemberFindUsernameRequestDTO request);

    MemberFindPasswordDTO.MemberFindPasswordResponseDTO getPassword(MemberFindPasswordDTO.MemberFindPasswordRequestDTO request);

    // 매일 자정에 실행
    void deleteOldInactiveMembers(long days);
}
