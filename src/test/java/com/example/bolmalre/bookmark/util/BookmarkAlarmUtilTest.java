package com.example.bolmalre.bookmark.util;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.domain.enums.Genre;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.infrastructure.BookmarkRepository;
import com.example.bolmalre.bookmark.web.port.BookmarkService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.MAIL_NOT_SEND;
import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BookmarkAlarmUtilTest {

    @InjectMocks
    BookmarkAlarmUtil bookmarkAlarmUtil;

    @Mock
    BookmarkService bookmarkService;

    @Mock
    BookmarkRepository bookmarkRepository;

    List<Artist> testArtists;

    Member testMember;

    Bookmark testBookmark1;

    Bookmark testBookmark2;

    @BeforeEach
    void setUp() {

        testMember = Member.builder()
                .id(1L)
                .username("test123")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .status(Status.ACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        Artist testArtist1 = Artist.builder()
                .id(1L)
                .name("test1")
                .genre(Genre.A)
                .profileImagePath("test")
                .build();

        Artist testArtist2 = Artist.builder()
                .id(2L)
                .name("test1")
                .genre(Genre.A)
                .profileImagePath("test")
                .build();

        testArtists = List.of(testArtist1, testArtist2);

        testBookmark1 = Bookmark.builder()
                .member(testMember)
                .artist(testArtist1)
                .build();

        testBookmark2 = Bookmark.builder()
                .member(testMember)
                .artist(testArtist2)
                .build();
    }


    @Test
    @DisplayName("sendMail() 을 이용하여 찜알림 메일 로직을 호출 할 수 있다")
    public void sendMail_success() throws MessagingException {
        //given
        when(bookmarkRepository.findByArtistIn(testArtists)).thenReturn(List.of(testBookmark1, testBookmark2));

        //when
        bookmarkAlarmUtil.sendMail(testArtists);

        //then
        verify(bookmarkService, times(2)).bookmarkAlarm(any());
    }


    @Test
    @DisplayName("메일 전송에 실패하면 정해진 예외를 반환한다")
    public void sendMail_fail() throws MessagingException {
        // given
        when(bookmarkRepository.findByArtistIn(anyList()))
                .thenReturn(List.of(testBookmark1, testBookmark2));

        doThrow(new MessagingException()).when(bookmarkService).bookmarkAlarm(anyString());

        // when & then
        assertThatThrownBy(() -> bookmarkAlarmUtil.sendMail(testArtists))
                .isInstanceOf(MailHandler.class)
                .hasFieldOrPropertyWithValue("code", MAIL_NOT_SEND);
    }

}