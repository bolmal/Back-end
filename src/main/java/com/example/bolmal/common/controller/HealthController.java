package com.example.bolmal.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "서버 안정성 체크 API")
public class HealthController {

    @GetMapping
    @Operation(summary = "서버 안정성을 체크하기 위한 API 입니다")
    public String health() {
        return "healthy!8";
    }
}
