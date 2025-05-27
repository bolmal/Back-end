package com.example.bolmalre.domain.alarm.domain;

import com.example.bolmalre.global.domain.BaseEntity;
import com.example.bolmalre.domain.concert.domain.Concert;
import com.example.bolmalre.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Alarm extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert")
    private Concert concert;


}
