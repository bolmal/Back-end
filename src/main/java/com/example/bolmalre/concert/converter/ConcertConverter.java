package com.example.bolmalre.concert.converter;

import com.example.bolmalre.concert.domain.ConcertPerformanceRound;
import com.example.bolmalre.concert.domain.ConcertPrice;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class ConcertConverter {


    // 티켓 오픈 라운드 + 날짜
    public String convertTicketRoundListToString(List<ConcertTicketRound> ctr) {
        if (ctr.isEmpty()) return "티켓팅 일정 없음";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E) HH:mm", Locale.KOREAN);
        return ctr.stream()
                .map(round -> round.getTicketRound() + "(" + round.getTicketOpenDate().format(formatter) + ")")
                .collect(Collectors.joining(", "));
    }

    // 간략화된 티켓 오픈 날짜
    public String convertTicketOpenDate(ConcertTicketRound concertTicketRound) {
        if (concertTicketRound == null) return "티켓팅 일정 없음";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E) HH:mm", Locale.KOREAN);

        return concertTicketRound.getTicketOpenDate().format(formatter);
    }

    // 티켓 가격
    public String convertTicketPriceListToString(List<ConcertPrice> cp) {
        if (cp.isEmpty()) return "가격 정보 없음";

        return cp.stream()
                .map(price -> price.getSeatType() + ": " + String.format("%,d원", price.getPrice()))
                .collect(Collectors.joining(", "));
    }

    //  공연 일시
    public String convertConcertPerformanceRoundListToString(List<ConcertPerformanceRound> cpr) {
        if (cpr.isEmpty()) return "공연 날짜 정보 없음";

        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E)", Locale.KOREAN);
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

    public String convertConcertPerformanceRoundToSimpleDate(List<ConcertPerformanceRound> cpr) {
        if (cpr.isEmpty()) return "공연 날짜 정보 없음";

        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");

        LocalDateTime firstConcertDate = cpr.get(0).getConcertDate();
        String firstDate = firstConcertDate.format(fullDateFormatter);


        LocalDateTime lastConcertDate = cpr.get(cpr.size() - 1).getConcertDate();
        String lastDay = lastConcertDate.format(dayFormatter);


        String firstYearMonth = firstConcertDate.format(DateTimeFormatter.ofPattern("yyyy.MM."));

        return firstConcertDate.getMonthValue() == lastConcertDate.getMonthValue()
                ? firstDate + " - " + lastDay
                : firstDate + " - " + lastConcertDate.format(fullDateFormatter);
    }


}
