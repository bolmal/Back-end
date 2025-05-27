package com.example.bolmalre.domain.alarm.infrastructure;

import com.example.bolmalre.domain.alarm.domain.Alarm;
import com.example.bolmalre.domain.concert.domain.Concert;
import com.example.bolmalre.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {

    boolean existsByMemberAndConcert(Member member, Concert concert);

    List<Alarm> findByMember(Member member);

    List<Alarm> findByConcert(Concert concert);
}
