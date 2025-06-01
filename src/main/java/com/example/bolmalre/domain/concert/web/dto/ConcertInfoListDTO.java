package com.example.bolmalre.domain.concert.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ConcertInfoListDTO {

    private List<ConcertPageDTO.ConcertInfoDTO> concertInfoDTOList;

    public static ConcertInfoListDTO of(List<ConcertPageDTO.ConcertInfoDTO> concertInfoDTOList) {
        return ConcertInfoListDTO.builder()
                .concertInfoDTOList(concertInfoDTOList)
                .build();
    }
}
