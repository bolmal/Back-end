package com.example.bolmalre.alarm.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
@Tag(name = "알림 기능 API")
public class AlarmController {

    /**
     알림 등록하기

     공연알림을 등록하는 기능입니다
     등록하면 회원의 알림 쿠폰이 하나 줄어들고

     정해진 알림 날짜가 되면 알림을 전송합니다

     알림 설정이 완료되면 알림 설정확인 메일을 전송합니다
     알림받을 공연정보, 티켓팅 정보 포함
     * */

    /**
     *
     * 등록 확인창 조회 API
     *
     * 이것도 필요한가
     *
     * */

    /**
     * 알림 설정해둔 공연 조회
     *
     * */



    /**
     * 회원이 알림받기 등록한 공연의 공지사항 보여주기
     *
     * 한 페이지당 다섯개 씩 노출
     * 공연, 공지사항 제목, 작성일
     *
     * */
}
