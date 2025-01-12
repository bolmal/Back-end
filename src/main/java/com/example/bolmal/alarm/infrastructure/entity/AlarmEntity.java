package com.example.bolmal.alarm.infrastructure.entity;

import com.example.bolmal.common.domain.BaseEntity;
import com.example.bolmal.concert.infrastructure.entity.ConcertEntity;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AlarmEntity extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_entity")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_entity")
    private ConcertEntity concertEntity;


}
