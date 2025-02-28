package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT DISTINCT c FROM Concert c " +
            "LEFT JOIN fetch ConcertTicketRound ctr on c.id = ctr.concert.id " +
            "WHERE ctr.ticketOpenDate BETWEEN :now AND :oneWeekLater")
    List<Concert> findConcertsWithTicketsOpeningInOneWeek(@Param("now") LocalDateTime now,
                                                          @Param("oneWeekLater") LocalDateTime oneWeekLater);

    // 최신 등록순 (createdAt 기준 내림차순)
    Page<Concert> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 티켓 오픈 날짜 가까운 순 (ticketOpenDate 기준 오름차순)
    @Query("""
    SELECT c FROM Concert c
    WHERE c.id IN (
        SELECT sub.concertId FROM (
            SELECT tr.concert.id AS concertId, MIN(tr.ticketOpenDate) AS earliestDate
            FROM ConcertTicketRound tr
            WHERE tr.ticketOpenDate >= CURRENT_TIMESTAMP
            GROUP BY tr.concert.id
        ) sub
    )
    ORDER BY (SELECT MIN(tr2.ticketOpenDate) FROM ConcertTicketRound tr2 WHERE tr2.concert = c)
""")
    Page<Concert> findAllOrderByTicketOpenDate(Pageable pageable);

    // 인기순 (dailyViewCount 기준 내림차순)
    Page<Concert> findAllByOrderByDailyViewCountDesc(Pageable pageable);
}

