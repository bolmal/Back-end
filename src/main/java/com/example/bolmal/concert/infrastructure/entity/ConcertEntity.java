package com.example.bolmal.concert.infrastructure.entity;

import com.example.bolmal.alarm.infrastructure.entity.AlarmEntity;
import com.example.bolmal.artist.infrastructure.entity.ArtistEntity;
import com.example.bolmal.common.domain.BaseEntity;
import com.example.bolmal.concert.domain.enums.ConcertRound;
import com.example.bolmal.concert.domain.enums.OnlineStore;
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
public class ConcertEntity extends BaseEntity {

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
    private String concertRuntime;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer concertAge;

    private String viewingRestri;

    @Column(nullable = false)
    private Integer recommendRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OnlineStore onlineStore;

    @OneToMany(mappedBy = "concertEntity", cascade = CascadeType.ALL)
    private List<AlarmEntity> alarmEntities = new ArrayList<>();

    @OneToMany(mappedBy = "concertEntity", cascade = CascadeType.ALL)
    private List<ArtistEntity> artistEntities = new ArrayList<>();

}
