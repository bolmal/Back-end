package com.example.bolmalre.domain.concert.web.controller;

import com.example.bolmalre.domain.concert.service.SaveConcertService;
import com.example.bolmalre.domain.concert.web.dto.SaveConcertDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "단일 콘서트 저장 API", description = """
콘서트 단일 정보를 저장합니다.<br><br>
혹시 아래에 나와있는 예외 이외의 예외가 발생한다면 꼭 <b>전재연</b>에게 연락 주시기 바랍니다.<br><br>
예시 JSON입니다. 아래 형식에 맞게 전달해주세요:<br><br>
아래에 **명세서 노션 링크**가 있습니다. 반드시 참고하세요 <br><br>
https://fluoridated-quicksand-e52.notion.site/API-20541ea6586e8196afeed9e6ff786999 <br><br>
<pre>
{
  "concert_name": "2025 이승철 콘서트 〈오케스트락2〉 인천공연",
  "concert_poster": "https://ticketimage.interpark.com/Play/image/large/25/25007293_p.gif",
  "genre": "록/메탈",
  "concert_mood": "Grand",
  "concert_style": "Orchestra",
  "concert_type": "Concert",
  "casting": [
    {"name": "이승철1"},
    {"name": "이승철2"},
    {"name": "이승철3"}
  ],
  "performance_rounds": [
    {
      "round": 1,
      "datetime": "2025-08-02T16:00:00"
    }
  ],
  "venue": "인스파이어 아레나",
  "running_time": 120,
  "price": {
    "type": 198000
  },
  "age_limit": "8세 이상",
  "booking_limit": "1인 10매 예매 가능",
  "selling_platform": "INTERPARK",
  "ticket_status": true,
  "ticket_open_dates": {
    "round": "2025-08-02T16:00:00"
  },
  "booking_link": "https://tickets.interpark.com/contents/notice/detail/9684",
  "additional_info": "이것은 하나의 문자열입니다."
}
</pre>  
""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "CONCERT4003",
            description = "이미 존재하는 콘서트를 주입하면 발생하는 예외입니다")
    })
    public void saveConcert(@RequestBody SaveConcertDTO.SaveRequestDTO concertRequest) {

        saveConcertService.saveConcert(concertRequest);
    }
}

