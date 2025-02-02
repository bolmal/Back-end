package com.example.bolmalre.concert.service;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ImageHandler;
import com.example.bolmalre.concert.converter.ConcertConverter;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertImage;
import com.example.bolmalre.concert.infrastructure.ConcertImageRepository;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import com.example.bolmalre.concert.web.dto.ConcertPageDTO;
import com.example.bolmalre.concert.web.port.ConcertService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Builder
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertImageRepository concertImageRepository;

    // 홈 광고 조회
    @Override
    public List<ConcertHomeDTO.AdvertisementConcertDTO> getAdConcertInfo() {

        Slice<Concert> adConcertList = concertRepository.findByAdvertisementIsTrue();


        return adConcertList.stream()
                .map(concert ->
                        ConcertConverter.toAdvertisementConcertDTO(concert, getConcertImageLink(concert)))
                .collect(Collectors.toList());

    }

    // 홈 지금 볼래 말래? ( 로그인 여부 판별 필) FIXME (추천도 점수 테이블 분리?)
    @Override
    public List<ConcertHomeDTO.RecommendConcertDTO> getRecommendConcertInfo() {

        boolean isLoggedIn = isUserLoggedIn();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dailyViewCount"));

        Slice<Concert> concerts = isLoggedIn
                ? null
                : concertRepository.findTodayTopViewedConcerts(pageable);


        return concerts.stream()
                .map(concert ->
                        ConcertConverter.toRecommendConcertDTO(concert, getConcertImageLink(concert)))
                .collect(Collectors.toList());

    }

    // 홈 이번주 가장 인기 있는 티켓( 주간 조회수 기준) FIXME( 알림 개수 추가 필요 )
    @Override
    public List<ConcertHomeDTO.WeekHotConcertDTO> getWeekHotConcertInfo() {

        Pageable pageable = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "weeklyViewCount"));

        Slice<Concert> weekHotConcertList = concertRepository.findWeeklyTopViewedConcerts(pageable);

        return weekHotConcertList.stream()
                .map(concert ->
                        ConcertConverter.toWeekHotConcertDTO(concert, getConcertImageLink(concert)))
                .collect(Collectors.toList());
    }

    // 콘서트 페이지 (20개씩 페이징)
    @Override
    public List<ConcertPageDTO.ConcertInfoDTO> getConcertPageInfo(int page, int size) {
        return List.of();
    }

    // 콘서트 상세 정보 페이지
    @Override
    public ConcertDetailPageDTO.ConcertDetailDTO getConcertDetailInfo() {
        return null;
    }

    // 로그인 여부 확인
    private boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    // 이미지 가져오기
    private String getConcertImageLink(Concert concert) {
        return concertImageRepository.findByConcert(concert)
                .map(ConcertImage::getImageLink)
                .orElseThrow(() -> new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND));
    }
}
