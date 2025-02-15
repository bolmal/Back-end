package com.example.bolmalre.concert.service;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.infrastructure.ArtistRepository;
import com.example.bolmalre.concert.converter.SaveConcertConverter;
import com.example.bolmalre.concert.domain.*;
import com.example.bolmalre.concert.infrastructure.*;
import com.example.bolmalre.concert.web.dto.SaveConcertDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SaveConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertPriceRepository concertPriceRepository;
    private final ConcertPerformanceRoundRepository concertPerformanceRoundRepository;
    private final ConcertTicketRoundRepository concertTicketRoundRepository;
    private final ConcertArtistRepository concertArtistRepository;
    private final ArtistRepository artistRepository;

    public void saveConcerts(List<SaveConcertDTO.SaveRequestDTO> concertRequestList) {
        for (SaveConcertDTO.SaveRequestDTO dto : concertRequestList) {
            saveConcert(dto);
        }
    }

    public void saveConcert(SaveConcertDTO.SaveRequestDTO dto) {
        // 1. Concert 엔티티 저장
        Concert concert = SaveConcertConverter.toConcert(dto);
        concertRepository.save(concert);

        // 2. ConcertPrice 엔티티 저장 (Map 데이터 처리)
        saveConcertPrices(dto, concert);

        // 3. ConcertPerformanceRound 엔티티 저장
        saveConcertPerformanceRounds(dto, concert);

        // 4. ConcertTicketRound 엔티티 저장 (Map 데이터 처리)
        saveConcertTicketRounds(dto, concert);


        // 5. 아티스트 정보 저장 (다대다 관계)
        saveConcertArtist(dto, concert);

    }

    private void saveConcertPrices(SaveConcertDTO.SaveRequestDTO dto, Concert concert) {
        if (dto.getPrice() != null && !dto.getPrice().isEmpty()) {
            List<ConcertPrice> prices = SaveConcertConverter.toConcertPrices(dto.getPrice(), concert);
            concertPriceRepository.saveAll(prices);
        }
    }

    private void saveConcertPerformanceRounds(SaveConcertDTO.SaveRequestDTO dto, Concert concert) {
        if (dto.getPerformanceRounds() != null && !dto.getPerformanceRounds().isEmpty()) {
            List<ConcertPerformanceRound> rounds = dto.getPerformanceRounds().stream()
                    .map(roundDTO -> SaveConcertConverter.toConcertPerformanceRound(roundDTO, concert))
                    .collect(Collectors.toList());
            concertPerformanceRoundRepository.saveAll(rounds);
        }
    }

    private void saveConcertTicketRounds(SaveConcertDTO.SaveRequestDTO dto, Concert concert) {
        if (dto.getTicketOpenDates() != null && !dto.getTicketOpenDates().isEmpty()) {
            List<ConcertTicketRound> ticketRounds = SaveConcertConverter.toConcertTicketRounds(dto.getTicketOpenDates(), concert);
            concertTicketRoundRepository.saveAll(ticketRounds);
        }
    }

    private void saveConcertArtist(SaveConcertDTO.SaveRequestDTO dto, Concert concert) {
        if (dto.getCasting() != null && !dto.getCasting().isEmpty()) {
            List<ConcertArtist> concertArtists = dto.getCasting().stream()
                    .map(artistDTO -> {
                        // Artist 조회 또는 생성
                        Artist artist = artistRepository.findByName(artistDTO.getName())
                                .orElseGet(() -> {
                                    Artist newArtist = Artist.builder()
                                            .name(artistDTO.getName())
                                            .build();
                                    return artistRepository.save(newArtist);  // 새로운 Artist 저장
                                });

                        // ConcertArtist 생성
                        return ConcertArtist.builder()
                                .artist(artist)
                                .concert(concert)
                                .build();
                    })
                    .collect(Collectors.toList());
            concertArtistRepository.saveAll(concertArtists);
        }
    }
}
