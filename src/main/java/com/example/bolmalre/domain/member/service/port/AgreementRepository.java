package com.example.bolmalre.domain.member.service.port;

import com.example.bolmalre.domain.member.domain.Agreement;
import com.example.bolmalre.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    void deleteAllByMemberIn(List<Member> members);
}
