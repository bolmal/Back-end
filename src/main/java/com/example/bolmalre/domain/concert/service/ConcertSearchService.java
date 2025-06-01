package com.example.bolmalre.domain.concert.service;

import com.example.bolmalre.domain.artist.infrastructure.ArtistRepository;
import com.example.bolmalre.domain.concert.domain.Concert;
import com.example.bolmalre.domain.concert.domain.ConcertTicketRound;
import com.example.bolmalre.domain.concert.infrastructure.*;
import com.example.bolmalre.domain.concert.web.dto.ConcertInfoListDTO;
import com.example.bolmalre.domain.concert.web.dto.ConcertPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertSearchService {

    private final ConcertRepository concertRepository;


    @Transactional(readOnly = true)
    public ConcertInfoListDTO searchConcertsByName(String keyword) {

        /**
         * 해당 키워드를 concertName으로 지니는 모든 콘서트를 조회하고
         *
         * 그걸 DTO에 맞게 후처리함
         * */
        List<Concert> concerts = concertRepository.findByConcertNameContainingIgnoreCase(keyword);

        List<ConcertPageDTO.ConcertInfoDTO> dtoList = concerts.stream()
                .map(concert -> {
                    ConcertTicketRound round = concert.getConcertTicketRounds().stream()
                            .min(Comparator.comparing(ConcertTicketRound::getTicketOpenDate))
                            .orElse(null);

                    return ConcertPageDTO.ConcertInfoDTO.builder()
                            .id(concert.getId())
                            .posterUrl(concert.getPosterUrl())
                            .concertName(concert.getConcertName())
                            .ticketRound(round != null ? round.getTicketRound() : null)
                            .ticketOpenDate(round != null ? round.getTicketOpenDate() : null)
                            .concertDate(
                                    concert.getConcertPerformanceRounds().isEmpty()
                                            ? null
                                            : concert.getConcertPerformanceRounds().get(0).getConcertDate().toString()
                            )
                            .build();
                })
                .toList();

        return ConcertInfoListDTO.of(dtoList);
    }

}
