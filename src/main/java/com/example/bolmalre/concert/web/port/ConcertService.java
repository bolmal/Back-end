package com.example.bolmalre.concert.web.port;

import com.example.bolmalre.concert.domain.enums.SortType;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import com.example.bolmalre.concert.web.dto.ConcertPageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ConcertService {

    // 홈 광고 조회
    List<ConcertHomeDTO.AdvertisementConcertDTO> getAdConcertInfo();

    // 홈 지금 볼래 말래? ( 로그인 여부 판별 필)
    List<ConcertHomeDTO.RecommendConcertDTO> getRecommendConcertInfoBeforeLogin();

    // 홈 지금 볼래 말래? ( 로그인 여부 판별 필)
//    List<ConcertHomeDTO.RecommendConcertDTO> getRecommendConcertInfoAfterLogin(Long memberId);

    // 홈 이번주 가장 인기 있는 티켓
    List<ConcertHomeDTO.WeekHotConcertDTO> getWeekHotConcertInfo();

    // 콘서트 페이지 (20개씩)
    Page<ConcertPageDTO.ConcertInfoDTO> getConcertPageInfo(int page, SortType sortType);

    // 콘서트 상세 정보 페이지
    ConcertDetailPageDTO.ConcertDetailDTO getConcertDetailInfo(Long concertId);

}
