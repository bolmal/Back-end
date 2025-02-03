package com.example.bolmalre.alarm.web.controller;

import com.example.bolmalre.alarm.web.port.AlarmService;
import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
@Tag(name = "알림 기능 API")
public class AlarmController {

    private final AlarmService alarmService;


    /**
     * 알림 구매하기
     * */
    @Operation(summary = "알림권 구매 API")
    @PatchMapping("/subscribes")
    public ApiResponse<String> subscribe(@AuthenticationPrincipal UserDetails userDetails) {

        alarmService.subscribe(userDetails.getUsername());
        return ApiResponse.onSuccess("구매가 성공적으로 완료되었습니다");
    }


    /**
     알림 등록하기

     공연알림을 등록하는 기능입니다
     등록하면 회원의 알림 쿠폰이 하나 줄어들고

     정해진 알림 날짜가 되면 알림을 전송합니다

     알림 설정이 완료되면 알림 설정확인 메일을 전송합니다
     알림받을 공연정보, 티켓팅 정보 포함
     * */
    @Operation(summary = "알림 등록 API")
    @PostMapping("")
    public void register(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam Long concertId) {

    }


    /**
     * 알림 설정해둔 공연 조회
     *
     * */
    @Operation(summary = "알림설정한 공연 조회 API")
    @GetMapping("")
    public void get() {
    }


    /**
     * 회원이 알림받기 등록한 공연의 공지사항 보여주기
     *
     * 한 페이지당 다섯개 씩 노출
     * 공연, 공지사항 제목, 작성일
     *
     * */
    @Operation(summary = "공지사항 조회 API")
    @GetMapping("/notions")
    public void notions() {
    }

    // 알림 전송하기
}
