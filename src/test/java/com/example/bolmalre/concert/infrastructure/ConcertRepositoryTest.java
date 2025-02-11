package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import com.example.bolmalre.member.service.port.LocalDateHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@DataJpaTest
@ExtendWith(MockitoExtension.class)
class ConcertRepositoryTest {

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertTicketRoundRepository concertTicketRoundRepository;

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
                .concertPlace("서울 공연장")
                .posterUrl("https://example.com/poster.jpg")
                .concertRuntime(100)
                .concertAge("12세 이상")
                .maxTicketsPerPerson("test")
                .onlineStore("예매 사이트")
                .onlineStoreLink("https://example.com/booking")
                .description("테스트 콘서트 설명")
                .ticketStatus(true)
                .build();

        Concert testConcert2 = Concert.builder()
                .concertName("test2")
                .concertPlace("서울 공연장")
                .posterUrl("https://example.com/poster.jpg")
                .concertRuntime(100)
                .concertAge("12세 이상")
                .maxTicketsPerPerson("test")
                .onlineStore("예매 사이트")
                .onlineStoreLink("https://example.com/booking")
                .description("테스트 콘서트 설명")
                .ticketStatus(true)
                .build();

        Concert testConcert3 = Concert.builder()
                .concertName("test3")
                .concertPlace("서울 공연장")
                .posterUrl("https://example.com/poster.jpg")
                .concertRuntime(100)
                .concertAge("12세 이상")
                .maxTicketsPerPerson("test")
                .onlineStore("예매 사이트")
                .onlineStoreLink("https://example.com/booking")
                .description("테스트 콘서트 설명")
                .ticketStatus(true)
                .build();

        ConcertTicketRound round1 = ConcertTicketRound.builder()
                .concert(testConcert1)
                .ticketRound("1차 예매")
                .ticketOpenDate(now.plusDays(3))
                .build();

        ConcertTicketRound round2 = ConcertTicketRound.builder()
                .concert(testConcert2)
                .ticketRound("1차 예매")
                .ticketOpenDate(now.plusDays(5))
                .build();

        ConcertTicketRound round3 = ConcertTicketRound.builder()
                .concert(testConcert3)
                .ticketRound("1차 예매")
                .ticketOpenDate(now.plusDays(10)) // 7일 이후라서 제외
                .build();

        concertRepository.save(testConcert1);
        concertRepository.save(testConcert2);
        concertRepository.save(testConcert3);

        concertTicketRoundRepository.save(round1);
        concertTicketRoundRepository.save(round2);
        concertTicketRoundRepository.save(round3);

        // When
        List<Concert> result = concertRepository.findConcertsWithTicketsOpeningInOneWeek(now, sevenDaysLater);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Concert::getConcertName)
                .containsExactlyInAnyOrder("test1", "test2"); // Concert 3은 포함되지 않아야 함
    }
}
