package com.example.bolmalre.concert.web.controller;

import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import com.example.bolmalre.concert.web.port.ConcertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Tag(name = "메인 페이지 조회 API")
@Builder
public class HomeConcertController {

    private final ConcertService concertService;
    private final GenericResponseService responseBuilder;

    @Operation(summary = "상단 광고 조회 API")
    @GetMapping("/advertisement")
    public ApiResponse<List<ConcertHomeDTO.AdvertisementConcertDTO>> getAdConcert() {

        List<ConcertHomeDTO.AdvertisementConcertDTO> response = concertService.getAdConcertInfo();

        return ApiResponse.onSuccess(response);

    }

    // FIXME (로그인 전후)
    @Operation(summary = "지금 볼래 말래? 조회 API",
            description = "로그인을 안했을 경우에는 ")
    @GetMapping("/recommend")
    public ApiResponse<List<ConcertHomeDTO.RecommendConcertDTO>> getRecommendConcert()
    {

        List<ConcertHomeDTO.RecommendConcertDTO> response = concertService.getRecommendConcertInfoBeforeLogin();

        return ApiResponse.onSuccess(response);

    }

    @Operation(summary = "이번주 가장 인기 있는 콘서트")
    @GetMapping("/hot")
    public ApiResponse<List<ConcertHomeDTO.WeekHotConcertDTO>> getWeekHotConcert(){

        List<ConcertHomeDTO.WeekHotConcertDTO> response = concertService.getWeekHotConcertInfo();

        return ApiResponse.onSuccess(response);
    }

}
