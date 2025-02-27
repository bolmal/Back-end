package com.example.bolmalre.concert.service;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ConcertHandler;
import com.example.bolmalre.concert.converter.ConcertConverter;
import com.example.bolmalre.concert.converter.ConcertDtoConverter;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertPerformanceRound;
import com.example.bolmalre.concert.domain.ConcertPrice;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dailyViewCount"));
        return convertConcertsToRecommendDTO(concertRepository.findTodayTopViewedConcerts(pageable));
    }

    // 홈 지금 볼래 말래? ( 로그인 이후) FIXME 추천도 점수 포함해야함
//    @Override
//    public List<ConcertHomeDTO.RecommendConcertDTO> getRecommendConcertInfoAfterLogin(Long memberId) {
//        return
//    }

    // 홈 이번주 가장 인기 있는 티켓 (주간 조회수 기준)
    @Override
    public List<ConcertHomeDTO.WeekHotConcertDTO> getWeekHotConcertInfo() {
        Pageable pageable = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "weeklyViewCount"));
        return convertConcertsToWeekHotDTO(concertRepository.findWeeklyTopViewedConcerts(pageable));
    }

    // 콘서트 카테고리 눌렀을 때 페이지
    @Override
    public List<ConcertPageDTO.ConcertInfoDTO> getConcertPageInfo(int page, int size) {
        return List.of();
    }

    // 콘서트 상세 정보
    @Override
    public ConcertDetailPageDTO.ConcertDetailDTO getConcertDetailInfo(Long concertId) {

        // 콘서트 찾기
        Concert targetConcert = findConcertById(concertId);

        // 해당 콘서트 관련 정보 (티켓 정보, 공연 날짜, 티켓 가격)
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



    /**
     * Concert 리스트를 받아서 RecommendConcertDTO 리스트로 변환
     */
    private List<ConcertHomeDTO.RecommendConcertDTO> convertConcertsToRecommendDTO(Slice<Concert> concerts) {
        return concerts.stream()
                .map(concert -> {
                    Optional<ConcertTicketRound> ticketRounds = getConcertTicketRounds(concert);
                    ConcertHomeDTO.DateRangeDTO dateRange = createConcertRoundDate(concert);

                    assert ticketRounds.orElse(null) != null;
                    return ConcertDtoConverter.toRecommendConcertDTO(concert, ticketRounds.orElse(null), dateRange);
                })
                .collect(Collectors.toList());
    }

    /**
     * Concert 리스트를 받아서 WeekHotConcertDTO 리스트로 변환
     */
    private List<ConcertHomeDTO.WeekHotConcertDTO> convertConcertsToWeekHotDTO(Slice<Concert> concerts) {
        return concerts.stream()
                .map(concert -> {
                    Optional<ConcertTicketRound> ticketRound = getConcertTicketRounds(concert);
                    ConcertHomeDTO.DateRangeDTO dateRange = createConcertRoundDate(concert);

                    assert ticketRound.orElse(null) != null;
                    return ConcertDtoConverter.toWeekHotConcertDTO(concert, ticketRound.orElse(null), dateRange);
                })
                .collect(Collectors.toList());
    }

    /**
     * Concert의 티켓 라운드 정보를 DTO 리스트로 변환
     */
    private Optional<ConcertTicketRound> getConcertTicketRounds(Concert concert) {
        LocalDateTime today = LocalDateTime.now();
        return concertTicketRoundRepository.findFirstByConcertAndTicketOpenDateAfterOrderByTicketOpenDateAsc(concert, today);
    }

    /**
     * Concert의 공연 일정 정보를 DTO로 변환
     */
    private ConcertHomeDTO.DateRangeDTO createConcertRoundDate(Concert concert) {
        List<ConcertPerformanceRound> rounds = concertPerformanceRoundRepository.findAllByConcert(concert);

        LocalDate startDate = rounds.stream()
                .filter(round -> round.getRound() == 1)
                .map(round -> round.getConcertDate().toLocalDate())
                .findFirst()
                .orElse(null);

        LocalDate endDate = rounds.stream()
                .max(Comparator.comparingInt(ConcertPerformanceRound::getRound))
                .map(round -> round.getConcertDate().toLocalDate())
                .orElse(null);

        return ConcertHomeDTO.DateRangeDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}