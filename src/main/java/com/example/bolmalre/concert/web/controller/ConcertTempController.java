package com.example.bolmalre.concert.web.controller;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.service.ConcertTempService;
import com.example.bolmalre.concert.web.dto.SaveDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
@Tag(name = "콘서트 기능 임시 API")
@Builder
public class ConcertTempController {

    private final ConcertTempService concertTempService;

    @Operation(summary = "콘서트 저장 임시 API")
    @PostMapping("")
    public void save(@RequestBody SaveDTO.SaveRequestDTO requestDTO){
        concertTempService.save(requestDTO);
    }
}

