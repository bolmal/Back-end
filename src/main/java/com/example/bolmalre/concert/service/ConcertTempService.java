package com.example.bolmalre.concert.service;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.infrastructure.ArtistRepository;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.infrastructure.BookmarkRepository;
import com.example.bolmalre.bookmark.util.BookmarkAlarmUtil;
import com.example.bolmalre.bookmark.web.port.BookmarkService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.concert.converter.ConcertArtistTempConverter;
import com.example.bolmalre.concert.converter.ConcertTempConverter;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertArtist;
import com.example.bolmalre.concert.infrastructure.ConcertArtistRepository;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.concert.web.dto.SaveDTO;
import com.example.bolmalre.member.domain.Member;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConcertTempService {

    private final ConcertRepository concertRepository;

    private final BookmarkAlarmUtil bookmarkAlarmUtil;
    private final ConcertArtistRepository concertArtistRepository;
    private final ArtistRepository artistRepository;

    public void save(SaveDTO.SaveRequestDTO request){

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
