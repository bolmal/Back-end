package com.example.bolmal.member.web.port;

import com.example.bolmal.member.service.port.LocalDate;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.dto.MemberProfileDTO;
import com.example.bolmal.member.web.dto.MemberUpdateDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request);

    MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request,
                                                   String username);

    MemberProfileDTO.MemberProfileResponseDTO get(String username);

    void delete(String username, LocalDate localDate);

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    void deleteOldInactiveMembers();
}
