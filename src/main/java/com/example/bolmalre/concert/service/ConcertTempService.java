package com.example.bolmalre.concert.service;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.infrastructure.ArtistRepository;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.infrastructure.BookmarkRepository;
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

    // 다른 의존성과 너무 결합이 많이 되어있다.....해결해야할듯
    private final BookmarkService bookmarkService;
    private final BookmarkRepository bookmarkRepository;
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

        // 그 콘서트 아티스트를 통해서 북마크를 추출해서
        List<Bookmark> byArtistIn = bookmarkRepository.findByArtistIn(artists);

        // 북마크에서 회원의 이메일을 추출해서 메일로 전송한다
        sendMail(byArtistIn);
    }




    private void sendMail(List<Bookmark> byArtistIn) {
        List<String> emails = byArtistIn.stream()
                .map(Bookmark::getMember)
                .map(Member::getEmail)
                .toList();

        emails.forEach(email -> {
            try {
                bookmarkService.bookmarkAlarm(email);
            } catch (MessagingException e) {
                throw new MailHandler(ErrorStatus.MAIL_NOT_SEND);
            }
        });
    }
}
