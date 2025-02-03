package com.example.bolmalre.concert.service;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.infrastructure.ArtistRepository;
import com.example.bolmalre.bookmark.util.BookmarkMailUtil;
import com.example.bolmalre.concert.converter.ConcertArtistTempConverter;
import com.example.bolmalre.concert.converter.ConcertTempConverter;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertArtist;
import com.example.bolmalre.concert.infrastructure.ConcertArtistRepository;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.concert.web.dto.SaveConcertDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConcertTempService {

    private final ConcertRepository concertRepository;

    private final BookmarkMailUtil bookmarkAlarmUtil;
    private final ConcertArtistRepository concertArtistRepository;
    private final ArtistRepository artistRepository;

    public void save(SaveConcertDTO.SaveRequestDTO request){

        List<Long> artistId = request.getArtistId();

        // 콘서트를 저장하고
        Concert newConcert = ConcertTempConverter.toConcert(request);
        concertRepository.save(newConcert);

        // 콘서트에 참여하는 아티스트를 저장하고
        List<Artist> artists = artistRepository.findByIdIn(artistId);

        List<ConcertArtist> concertArtists = artists.stream()
                .map(artist -> ConcertArtistTempConverter.toConcertArtist(newConcert,artist))
                .toList();
        concertArtistRepository.saveAll(concertArtists);

        bookmarkAlarmUtil.sendMail(artists);
    }
}
