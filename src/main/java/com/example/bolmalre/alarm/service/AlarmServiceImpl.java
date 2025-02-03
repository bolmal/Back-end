package com.example.bolmalre.alarm.service;

import com.example.bolmalre.alarm.domain.Alarm;
import com.example.bolmalre.alarm.infrastructure.AlarmRepository;
import com.example.bolmalre.alarm.web.port.AlarmService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.AlarmHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ConcertHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final ConcertRepository concertRepository;

    @Override
    public void subscribe(String username){
        Member findMember = findMemberByUsername(username);

        Member.alarmAccountPlus(findMember);
    }

    @Override
    public void register(String username, Long concertId){

        Member findMember = findMemberByUsername(username);
        Concert findConcert = findConcertById(concertId);

        Alarm newAlarm = Alarm.builder()
                .member(findMember)
                .concert(findConcert)
                .build();

        validAlarmAccount(findMember);
        Member.bookmarkAccountMinus(findMember);
        validAlarmExist(findMember, findConcert);

        alarmRepository.save(newAlarm);
    }






    private static void validAlarmAccount(Member findMember) {
        if (findMember.getAlarmAccount().equals(0)){
            throw new AlarmHandler(ErrorStatus.ALARM_ACCOUNT_ZERO);
        }
    }

    private void validAlarmExist(Member findMember, Concert findConcert) {
        boolean valid = alarmRepository.existsByMemberAndConcert(findMember, findConcert);
        if (valid){
            throw new AlarmHandler(ErrorStatus.ALARM_EXISTS);
        }
    }

    private Concert findConcertById(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(()-> new ConcertHandler(ErrorStatus.CONCERT_NOT_FOUND));
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
