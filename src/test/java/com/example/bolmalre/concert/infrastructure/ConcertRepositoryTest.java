package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.enums.ConcertRound;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
import com.example.bolmalre.member.infrastructure.LocalDateHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@DataJpaTest
@ExtendWith(MockitoExtension.class)
class ConcertRepositoryTest {

    @Autowired
    private ConcertRepository concertRepository;

    @Mock
    LocalDateHolder localDateHolder;

    @Test
    @DisplayName("특정 기간 동안 티켓 오픈되는 콘서트 목록을 조회할 수 있다")
    void findConcertsWithTicketOpeningInAWeek_success() {
        // Given
        when(localDateHolder.now()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));

        LocalDateTime now = localDateHolder.now();
        LocalDateTime sevenDaysLater = now.plusDays(7);

        Concert testConcert1 = Concert.builder()
                .concertName("test1")
                .concertRound(ConcertRound.FIRST)
                .concertDate(LocalDate.of(1, 1, 1))
                .ticketOpenDate(now.plusDays(3))
                .concertRuntime("test1")
                .concertPlace("test1")
                .price(0)
                .concertAge(0)
                .viewingRestrict("test1")
                .onlineStore(OnlineStore.INTERPARK)
                .viewCount(0)
                .recommendRate(0)
                .advertisement(true)
                .posterUrl("test1")
                .concertArtists(new ArrayList<>())
                .build();

        Concert testConcert2 = Concert.builder()
                .concertName("test2")
                .concertRound(ConcertRound.FIRST)
                .concertDate(LocalDate.of(1,1,1))
                .ticketOpenDate(now.plusDays(7))
                .concertRuntime("test2")
                .concertPlace("test2")
                .price(0)
                .concertAge(0)
                .viewingRestrict("test2")
                .onlineStore(OnlineStore.INTERPARK)
                .viewCount(0)
                .recommendRate(0)
                .advertisement(true)
                .posterUrl("test2")
                .concertArtists(new ArrayList<>())
                .build();

        Concert testConcert3 = Concert.builder()
                .concertName("test3")
                .concertRound(ConcertRound.FIRST)
                .concertDate(LocalDate.of(1,1,1))
                .ticketOpenDate(now.plusDays(8))
                .concertRuntime("test3")
                .concertPlace("test3")
                .price(0)
                .concertAge(0)
                .viewingRestrict("test3")
                .onlineStore(OnlineStore.INTERPARK)
                .viewCount(0)
                .recommendRate(0)
                .advertisement(true)
                .posterUrl("test3")
                .concertArtists(new ArrayList<>())
                .build();

        concertRepository.save(testConcert1);
        concertRepository.save(testConcert2);
        concertRepository.save(testConcert3);

        // When
        List<Concert> result = concertRepository.findConcertsWithTicketOpeningInAWeek(now, sevenDaysLater);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Concert::getConcertName)
                .containsExactlyInAnyOrder("test1", "test2"); // Concert 3은 포함되지 않아야 함
    }
}