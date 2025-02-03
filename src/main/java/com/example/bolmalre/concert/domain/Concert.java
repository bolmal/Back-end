package com.example.bolmalre.concert.domain;


import com.example.bolmalre.common.domain.BaseEntity;
import com.example.bolmalre.concert.domain.enums.ConcertRound;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
import com.example.bolmalre.member.domain.MemberProfileImage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private String concertName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConcertRound concertRound;

    @Column(nullable = false)
    private String concertPlace;

    @Column(nullable = false)
    private LocalDate concertDate;

    @Column(nullable = false)
    private LocalDateTime ticketOpenDate;

    @Column(nullable = false)
    private String concertRuntime;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer concertAge;

    @Column(nullable = false)
    private String viewingRestrict;

    @Column(nullable = false)
    private Integer recommendRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OnlineStore onlineStore;

    @Column(nullable = false)
    private Integer dailyViewCount;

    @Column(nullable = false)
    private Integer weeklyViewCount;

    @Column(nullable = false)
    private boolean advertisement;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<ConcertArtist> concertArtists = new ArrayList<>();


    public void increaseDailyViewCount() {
        this.dailyViewCount++;
    }

    public void increaseWeeklyViewCount() {
        this.weeklyViewCount++;
    }
}
