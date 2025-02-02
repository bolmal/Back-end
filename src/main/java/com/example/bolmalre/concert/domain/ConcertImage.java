package com.example.bolmalre.concert.domain;

import com.example.bolmalre.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ConcertImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이미지가 저장된 주소 링크
    @Column(name = "image_link", length = 1000)
    private String imageLink;

    @Column(nullable = false, columnDefinition = "text")
    private String fileName;

    @Column(nullable = false)
    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

}
