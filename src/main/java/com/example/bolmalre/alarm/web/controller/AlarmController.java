package com.example.bolmalre.alarm.web.controller;

import com.example.bolmalre.alarm.web.dto.AlarmReadDTO;
import com.example.bolmalre.alarm.web.port.AlarmService;
import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
@Tag(name = "알림 기능 API")
public class AlarmController {

    private final AlarmService alarmService;


    @Operation(summary = "알림권 구매 API")
    @PatchMapping("/subscribes")
    public ApiResponse<String> subscribe(@AuthenticationPrincipal UserDetails userDetails) {

        alarmService.subscribe(userDetails.getUsername());
        return ApiResponse.onSuccess("구매가 성공적으로 완료되었습니다");
    }



    @Operation(summary = "알림 등록 API")
    @PostMapping("")
    public ApiResponse<String> register(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam Long concertId) {
        alarmService.register(userDetails.getUsername(), concertId);
        return ApiResponse.onSuccess("알림 등록이 성공적으로 완료되었습니다");
    }


    @Operation(summary = "알림 설정한 공연 조회 API")
    @GetMapping("")
    public ApiResponse<List<AlarmReadDTO.AlarmReadRequestDTO>> get(@AuthenticationPrincipal UserDetails userDetails) {
        List<AlarmReadDTO.AlarmReadRequestDTO> result = alarmService.get(userDetails.getUsername());

        return ApiResponse.onSuccess(result);
    }


    /**
     * 회원이 알림받기 등록한 공연의 공지사항 보여주기
     *
     * 한 페이지당 다섯개 씩 노출
     * 공연, 공지사항 제목, 작성일
     *
     * 아직 개발계획 불투명
     * */
    @Operation(summary = "공지사항 조회 API")
    @GetMapping("/notions")
    public void notions() {
    }


    // 알림 전송하기
    @Operation(summary = "알림 전송 API")
    @PostMapping("/alarm")
    public ApiResponse<String> send(String email) throws MessagingException {
        alarmService.alarmMail(email);

        return ApiResponse.onSuccess("알림이 성공적으로 전송되었습니다");
    }
}
