package com.example.bolmalre.domain.concert.web.controller;

import com.example.bolmalre.domain.concert.service.ConcertSearchService;
import com.example.bolmalre.domain.concert.web.dto.ConcertInfoListDTO;
import com.example.bolmalre.global.apiPayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts/search")
@Tag(name = "콘서트 검색 관련 API")
public class ConcertSearchController {

    private final ConcertSearchService concertSearchService;


    @GetMapping("/names")
    @Operation(summary = "콘서트 제목 기반 검색 API")
    @Parameter(name = "keyword", description = "검색하고 싶은 키워드를 입력해주세요")
    public ApiResponse<ConcertInfoListDTO> getConcertByName(@RequestParam String keyword) {
        ConcertInfoListDTO concertInfoListDTO = concertSearchService.searchConcertsByName(keyword);

        return ApiResponse.onSuccess(concertInfoListDTO);
    }
}
