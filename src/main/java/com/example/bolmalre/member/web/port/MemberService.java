package com.example.bolmalre.member.web.port;

import com.example.bolmalre.member.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Scheduled;

public interface MemberService {
    MemberJoinDTO.MemberJoinResponseDTO joinMember(@Valid MemberJoinDTO.MemberJoinRequestDTO request);

    MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request, String username);

    MemberProfileDTO.MemberProfileResponseDTO get(String username);

    void delete(String username);

    void rollback(String username);

    String resetPassword(String username, MemberUpdateDTO.MemberPasswordUpdateRequestDTO request);

    void validPassword(String username, MemberPasswordValidDTO.MemberPasswordValidRequestDTO request);

    MemberFindUsernameDTO.MemberFindUsernameResponseDTO getUsername(MemberFindUsernameDTO.MemberFindUsernameRequestDTO request);

    MemberFindPasswordDTO.MemberFindPasswordResponseDTO getPassword(MemberFindPasswordDTO.MemberFindPasswordRequestDTO request);

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    void deleteOldInactiveMembers();
}
