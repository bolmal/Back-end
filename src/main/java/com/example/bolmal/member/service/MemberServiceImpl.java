package com.example.bolmal.member.service;



import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.service.port.MemberRepository;
import com.example.bolmal.member.util.BCrypt;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.port.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCrypt bCrypt;


    @Override
    public MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request){

        Member newMember= Member.JoinDTOto(request,bCrypt);

        Member savedMember = memberRepository.save(newMember);


        return MemberJoinDTO.MemberJoinResponseDTO.builder()
                .MemberId(savedMember.getId())
                .build();

    }
}
