package com.example.bolmal.member.mock;

import com.example.bolmal.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmal.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.service.port.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberRepository implements MemberRepository {

    private final AtomicLong counter = new AtomicLong(0);
    private final List<Member> data = new ArrayList<>();


    @Override
    public Optional<Member> findByUsername(String username) {

        // data에서 stream을 이용하여 순회
        // 그때 username이 같은 데이터가 있다면 반환한다
        return data.stream().filter(item-> item.getUsername().equals(username)).findAny();
    }

    @Override
    public Member save(Member member) {
        Member newMember =Member.builder()
                .id(counter.incrementAndGet())
                .username(member.getUsername())
                .password(member.getPassword())
                .name(member.getName())
                .nickname(member.getNickname())
                .role(Role.ROLE_USER)
                .phoneNumber(member.getPhoneNumber())
                .birthday(member.getBirthday())
                .email(member.getEmail())
                .status(Status.ACTIVE)
                .gender(member.getGender())
                .build();



        data.add(newMember);
        return newMember;
    }

    @Override
    public boolean existsByUsername(String username) {
        return data.stream().anyMatch(item -> item.getUsername().equals(username));
    }

    @Override
    public List<Member> findInactiveMembersForDeletion(Status status, LocalDateTime cutoffDate) {
        return data.stream()
                .filter(item -> item.getStatus().equals(status) && item.getInactiveDate().isBefore(cutoffDate))
                .toList();
    }

    @Override
    public void deleteAll(List<Member> membersToDelete) {
        data.removeAll(membersToDelete);
    }

    @Override
    public Member findByNameAndPhoneNumber(String name, String phoneNumber) {
        return data.stream()
                .filter(item -> item.getName().equals(name) && item.getPhoneNumber().equals(phoneNumber))
                .findAny()
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND)); // MemberHandler 생성자 호출 시 lambda 사용
    }

    public void clear() {
        data.clear();
        counter.set(0);
    }

}
