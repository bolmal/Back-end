package com.example.bolmalre.domain.concert.web.controller;

import com.example.bolmalre.domain.concert.service.SaveConcertService;
import com.example.bolmalre.domain.concert.web.dto.SaveConcertDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/save")
@Tag(name = "콘서트 정보 저장 API")
@Builder
@Slf4j
public class SaveConcertController {

    private final SaveConcertService saveConcertService;

    @PostMapping("/concerts")
    @Operation(summary = "콘서트 N개 저장 API",
    description = "여러개의 콘서트를 **리스트**를 통해 저장합니다")
    public void saveConcerts(@RequestBody List<SaveConcertDTO.SaveRequestDTO> concertRequestList) {

        log.info("Request received: {}", concertRequestList);
        saveConcertService.saveConcerts(concertRequestList);
    }


    @PostMapping("/concert")
    @Operation(summary = "단일 콘서트 저장 API")
    public void saveConcert(@RequestBody SaveConcertDTO.SaveRequestDTO concertRequest) {

        saveConcertService.saveConcert(concertRequest);
    }
}

