package com.example.bolmalre.concert.converter;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertPerformanceRound;
import com.example.bolmalre.concert.domain.ConcertPrice;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import com.example.bolmalre.concert.web.dto.ConcertPageDTO;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class ConcertConverter {



    public String convertTicketRoundListToString(List<ConcertTicketRound> ctr) {
        if (ctr.isEmpty()) return "티켓팅 일정 없음";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E) HH:mm", Locale.KOREAN);
        return ctr.stream()
                .map(round -> round.getTicketRound() + "(" + round.getTicketOpenDate().format(formatter) + ")")
                .collect(Collectors.joining(", "));
    }

    public String convertTicketPriceListToString(List<ConcertPrice> cp) {
        if (cp.isEmpty()) return "가격 정보 없음";

        return cp.stream()
                .map(price -> price.getSeatType() + ": " + String.format("%,d원", price.getPrice()))
                .collect(Collectors.joining(", "));
    }

    public String convertConcertPerformanceRoundListToString(List<ConcertPerformanceRound> cpr) {
        if (cpr.isEmpty()) return "공연 날짜 정보 없음";

        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E)", Locale.KOREAN);
        DateTimeFormatter shortDateFormatter = DateTimeFormatter.ofPattern("d (E)", Locale.KOREAN);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h a", Locale.ENGLISH);

        String firstDate = cpr.get(0).getConcertDate().format(fullDateFormatter);
        String time = cpr.get(0).getConcertDate().format(timeFormatter);

        String otherDates = cpr.stream()
                .skip(1)
                .map(round -> round.getConcertDate().format(shortDateFormatter))
                .collect(Collectors.joining(", "));

        return otherDates.isEmpty() ? firstDate + " " + time : firstDate + ", " + otherDates + " " + time;
    }
}
