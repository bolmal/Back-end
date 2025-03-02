package com.example.bolmalre.concert.service;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ConcertHandler;
import com.example.bolmalre.concert.converter.ConcertConverter;
import com.example.bolmalre.concert.converter.ConcertDtoConverter;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertPerformanceRound;
import com.example.bolmalre.concert.domain.ConcertPrice;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import com.example.bolmalre.concert.domain.enums.SortType;
import com.example.bolmalre.concert.infrastructure.*;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import com.example.bolmalre.concert.web.dto.ConcertPageDTO;
import com.example.bolmalre.concert.web.port.ConcertService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Builder
@Service
@Transactional
@RequiredArgsConstructor
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertImageRepository concertImageRepository;
    private final ConcertTicketRoundRepository concertTicketRoundRepository;
    private final ConcertPerformanceRoundRepository concertPerformanceRoundRepository;
    private final ConcertPriceRepository concertPriceRepository;
    private final ConcertConverter converter;
    private final Integer PAGE_SIZE = 20;

    // 홈 광고 조회
    @Override
    public List<ConcertHomeDTO.AdvertisementConcertDTO> getAdConcertInfo() {
        return concertRepository.findByAdvertisementIsTrue().stream()
                .map(ConcertDtoConverter::toAdvertisementConcertDTO)
                .collect(Collectors.toList());
    }

    // 홈 지금 볼래 말래? ( 로그인 이전 )
    @Override
    public List<ConcertHomeDTO.RecommendConcertDTO> getRecommendConcertInfoBeforeLogin() {
        Pageable pageable = PageRequest.of(0, 8);

        Slice<Concert> concerts = concertRepository.findTodayTopViewedConcerts(pageable);

        return concerts.getContent().stream()
                .map(concert -> {
                    ConcertTicketRound ctr = findNearestConcertTicket(concert.getId());

                    String round = ctr.getTicketRound();

                    /*String ticketOpenDate = converter.convertTicketOpenDate(ctr);*/
                    LocalDateTime ticketOpenDate = ctr.getTicketOpenDate();

                    String concertPerformanceDate = converter.convertConcertPerformanceRoundToSimpleDate(findConcertPerformanceRoundByConcert(concert));

                    return ConcertDtoConverter.toRecommendConcertDTO(concert, round, ticketOpenDate, concertPerformanceDate);
                }).toList();
    }



    // 홈 이번주 가장 인기 있는 티켓 (주간 조회수 기준)
    @Override
    public List<ConcertHomeDTO.WeekHotConcertDTO> getWeekHotConcertInfo() {
        Pageable pageable = PageRequest.of(0, 8);

        Slice<Concert> concerts = concertRepository.findWeeklyTopViewedConcerts(pageable);

        return concerts.getContent().stream()
                        .map(concert -> {
                            ConcertTicketRound ctr = findNearestConcertTicket(concert.getId());

                            String round = ctr.getTicketRound();

                            LocalDateTime ticketOpenDate = ctr.getTicketOpenDate();

                            String concertPerformanceDate = converter.convertConcertPerformanceRoundToSimpleDate(findConcertPerformanceRoundByConcert(concert));

                            return ConcertDtoConverter.toWeekHotConcertDTO(concert, round, ticketOpenDate, concertPerformanceDate);
                        }).toList();

    }

    // 콘서트 카테고리 눌렀을 때 페이지
    @Override
    public Page<ConcertPageDTO.ConcertInfoDTO> getConcertPageInfo(int page, SortType sortType) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Concert> concertPage = switch (sortType) {
            case LATEST -> concertRepository.findAllByOrderByCreatedAtDesc(pageable);
            case TICKET_OPEN -> concertRepository.findAllOrderByTicketOpenDate(pageable);
            case POPULAR -> concertRepository.findAllByOrderByDailyViewCountDesc(pageable);
        };
        System.out.println("콘서트 페이지 사이즈: " + concertPage.getTotalElements());
        concertPage.forEach(c -> System.out.println("콘서트 ID: " + c.getId()));
        return concertPage.map(this::convertToConcertInfoDTO);
    }



    // 콘서트 상세 정보
    @Override
    public ConcertDetailPageDTO.ConcertDetailDTO getConcertDetailInfo(Long concertId) {

        // 콘서트 찾기
        Concert targetConcert = findConcertById(concertId);

        // 해당 콘서트 관련 정보 (티켓 정보, 공연 날짜, 티켓 가격)
        // 이거 일단 첫번째 예매 날짜만 가져오도록 했는데 추가로 수정해야할듯 FIXME
        String ticketInfo = converter.convertTicketRoundListToString(findTicketRoundByConcert(targetConcert));

        String concertDate = converter.convertConcertPerformanceRoundListToString(findConcertPerformanceRoundByConcert(targetConcert));

        String ticketPrice = converter.convertTicketPriceListToString(findTicketPriceByConcert(targetConcert));


        // dto로 전환해서 뱉기
        return ConcertDtoConverter.toConcertDetailDTO(targetConcert, ticketInfo, concertDate,ticketPrice);
    }


    // 콘서트 찾기
    private Concert findConcertById(Long concertId) {

        return concertRepository.findById(concertId)
                .orElseThrow(() -> new ConcertHandler(ErrorStatus.CONCERT_NOT_FOUND));
    }

    // 티켓 공연 정보 다 가져오기
    private List<ConcertTicketRound> findTicketRoundByConcert(Concert concert) {
        return concertTicketRoundRepository.findAllByConcert(concert);
    }

    // 티켓 가격 정보 다 가져오기
    private List<ConcertPrice> findTicketPriceByConcert(Concert concert) {
        return concertPriceRepository.findAllByConcert(concert);
    }

    // 공연 날짜 정보 다 가져오기
    private List<ConcertPerformanceRound> findConcertPerformanceRoundByConcert(Concert concert) {
        return concertPerformanceRoundRepository.findAllByConcert(concert);
    }

    // 가장 가까운 티켓 예매 정보 가져오기
    private ConcertTicketRound findNearestConcertTicket(Long concertId) {
        return concertTicketRoundRepository.findNearestConcertTicket(LocalDateTime.now())
                .orElseThrow(() -> new ConcertHandler(ErrorStatus.CONCERT_TICKET_ROUND_NOT_FOUND));
    }

    private ConcertPageDTO.ConcertInfoDTO convertToConcertInfoDTO(Concert concert) {


        ConcertTicketRound ticketRound = findNearestConcertTicket(concert.getId());
        List<ConcertPerformanceRound> performanceRounds = findConcertPerformanceRoundByConcert(concert);

        return ConcertDtoConverter.toConcertInfoDTO(
                concert,
                ticketRound,
                ticketRound.getTicketOpenDate(),
                /*converter.convertTicketOpenDate(ticketRound),*/
                converter.convertConcertPerformanceRoundToSimpleDate(performanceRounds)
        );
    }





}