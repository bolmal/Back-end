package com.example.bolmalre.bookmark.util;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.infrastructure.BookmarkRepository;
import com.example.bolmalre.bookmark.web.port.BookmarkService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.member.domain.Member;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookmarkAlarmUtil {

    private final BookmarkService bookmarkService;
    private final BookmarkRepository bookmarkRepository;

    public void sendMail(List<Artist> artists) {

        List<Bookmark> byArtistIn = bookmarkRepository.findByArtistIn(artists);

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
