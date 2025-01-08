package com.example.bolmal.member.mock;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.service.port.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.bolmal.member.util.BCryptImpl.encode;

public class FakeMemberRepository implements MemberRepository {

    private final AtomicLong counter = new AtomicLong(0);
    private final List<Member> data = new ArrayList<>();


    @Override
    public Optional<Member> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Member save(Member member) {
        Member newMember =Member.builder()
                .id(counter.incrementAndGet())
                .username(member.getUsername())
                .password(encode(member.getPassword()))
                .name(member.getName())
                .nickname(member.getNickname())
                .role(Role.ROLE_USER)
                .phoneNumber(member.getPhoneNumber())
                .birthday(member.getBirthday())
                .email(member.getEmail())
                .status(Status.ACTIVE)
                .gender(member.getGender())
                .build();
    }
}
