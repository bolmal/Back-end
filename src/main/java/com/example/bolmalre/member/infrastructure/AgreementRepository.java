package com.example.bolmalre.member.infrastructure;

import com.example.bolmalre.member.domain.Agreement;
import com.example.bolmalre.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    void deleteAllByMemberIn(List<Member> members);
}
