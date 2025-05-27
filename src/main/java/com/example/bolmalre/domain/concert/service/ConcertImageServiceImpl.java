package com.example.bolmalre.domain.concert.service;

import com.example.bolmalre.domain.concert.domain.ConcertImage;
import com.example.bolmalre.domain.concert.infrastructure.ConcertImageRepository;
import com.example.bolmalre.domain.concert.web.dto.ConcertAdvResponse;
import com.example.bolmalre.domain.concert.web.port.ConcertImageService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Builder
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConcertImageServiceImpl implements ConcertImageService {

    private final ConcertImageRepository concertImageRepository;

    @Override
    public ConcertAdvResponse.ConcertAdvListResponse getConcertAdvListResponse() {
        List<ConcertImage> concertImages = concertImageRepository.findTop10ByOrderByIdDesc();

        return ConcertAdvResponse.ConcertAdvListResponse.of(concertImages);
    }
}
