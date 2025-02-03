package com.example.bolmalre.alarm.infrastructure;

import com.example.bolmalre.alarm.domain.Alarm;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {

    boolean existsByMemberAndConcert(Member member, Concert concert);
}
