package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    // FIXME (N+1 1차 해결 -> 추후 QueryDSL 고려)

    @Query("select distinct c from Concert c left join fetch ConcertImage ci on c.id = ci.concert.id where c.advertisement = true")
    Slice<Concert> findByAdvertisementIsTrue();

    // 지금 볼래말래[로그인 전] ( 그 날 조회수가 가장 높은 공연 조회 )
    @Query("select distinct c from Concert c left join fetch ConcertImage ci on c.id = ci.concert.id order by c.dailyViewCount desc")
    Slice<Concert> findTodayTopViewedConcerts(Pageable pageable);

    // 주간 조회수
    @Query("select distinct c from Concert c left join fetch ConcertImage ci on c.id = ci.concert.id order by c.weeklyViewCount desc")
    Slice<Concert> findWeeklyTopViewedConcerts(Pageable pageable);

    @Query("SELECT c FROM Concert c WHERE c.ticketOpenDate BETWEEN :startDate AND :endDate")
    List<Concert> findConcertsWithTicketOpeningInAWeek(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
