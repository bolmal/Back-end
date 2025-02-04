package com.example.bolmalre.concert.domain;


import com.example.bolmalre.common.domain.BaseEntity;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Concert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 콘서트(공연) 제목(이름)
    private String concertName;

    // 콘서트 포스터 URL
    private String posterUrl;

    // 콘서트(공연) 장소
    private String concertPlace;

    // 티켓 오픈 여부
    private Boolean ticketStatus;

    // 러닝타임
    private String concertRuntime;

    // 관람 연령
    private String concertAge;

    // 예매 제한
    private String maxTicketsPerPerson;

    // 티켓 구매 사이트 종류
    private String onlineStore;

    // 티켓팅 링크
    private String onlineStoreLink;

    // 공연 소개
    private String description;


    private Integer dailyViewCount;

    private Integer weeklyViewCount;

    private boolean advertisement;



    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<ConcertArtist> concertArtists = new ArrayList<>();


    public void increaseDailyViewCount() {
        this.dailyViewCount++;
    }

    public void increaseWeeklyViewCount() {
        this.weeklyViewCount++;
    }
}
