package com.example.bolmalre.domain.concert.web.dto;

import com.example.bolmalre.domain.concert.domain.ConcertImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ConcertAdvResponse {

    private Long id;
    private String imageUrl;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertAdvListResponse{

        List<ConcertAdvResponse> result;

        public static ConcertAdvListResponse of(List<ConcertImage> images) {
            List<ConcertAdvResponse> result = images.stream()
                    .map(image -> ConcertAdvResponse.builder()
                            .id(image.getId())
                            .imageUrl(image.getImageLink())
                            .build())
                    .collect(Collectors.toList());

            return ConcertAdvListResponse.builder()
                    .result(result)
                    .build();
        }
    }
}
