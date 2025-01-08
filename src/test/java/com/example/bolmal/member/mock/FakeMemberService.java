package com.example.bolmal.member.mock;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.infrastructure.MemberJpaRepository;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.port.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class FakeMemberService implements MemberService {

    FakeMemberRepository fakeMemberRepository= new FakeMemberRepository();
    FakeBCrypt fakeBCrypt = new FakeBCrypt();

    @Override
    public MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request) {

        fakeMemberRepository.save(Member.builder()
                .id(2L)
                .username(request.getUsername())
                .password(fakeBCrypt.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .role(Role.ROLE_USER)
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthDate())
                .email(request.getEmail())
                .status(Status.ACTIVE)
                .gender(request.getGender())
                .build());

        return MemberJoinDTO.MemberJoinResponseDTO.builder()
                .memberId(2L)
                .build();
    }
}
