package com.example.bolmalre.concert.web.controller;

import com.example.bolmalre.concert.service.SaveConcertService;
import com.example.bolmalre.concert.web.dto.SaveConcertDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts/save")
@Tag(name = "콘서트 정보 저장 API")
@Builder
@Slf4j
public class SaveConcertController {

    private final SaveConcertService saveConcertService;

    @PostMapping("")
    public void saveConcerts(@RequestBody List<SaveConcertDTO.SaveRequestDTO> concertRequestList) {

        log.info("Request received: {}", concertRequestList);
        saveConcertService.saveConcerts(concertRequestList);
    }
}

