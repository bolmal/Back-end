package com.example.bolmalre.bookmark.service;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.domain.enums.Genre;
import com.example.bolmalre.artist.infrastructure.ArtistRepository;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.infrastructure.BookmarkRepository;
import com.example.bolmalre.bookmark.web.dto.BookmarkGetArtistDTO;
import com.example.bolmalre.bookmark.web.dto.BookmarkRegisterDTO;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ArtistHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.BookmarkHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BookmarkServiceImplTest {

    @InjectMocks
    BookmarkServiceImpl bookmarkService;

    @Mock
    BookmarkRepository bookmarkRepository;

    @Mock
    ArtistRepository artistRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    Member testMember;

    Artist testArtist;

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

        testArtist = Artist.builder()
                .id(1L)
                .name("test_artist")
                .genre(Genre.A)
                .profileImagePath("test_profile_image")
                .build();
    }


    @Test
    @DisplayName("register() 을 이용하여 아티스트 찜 등록을 할 수 있다")
    public void register_success(){
        //given
        Member.bookmarkAccountPlus(testMember);

        BookmarkRegisterDTO.BookmarkRegisterRequestDTO dto = BookmarkRegisterDTO.BookmarkRegisterRequestDTO.builder()
                .artistId(1L)
                .build();

        Bookmark testArtist = Bookmark.builder()
                .id(1L)
                .member(testMember)
                .artist(this.testArtist)
                .build();

        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(artistRepository.findById(any())).thenReturn(Optional.of(this.testArtist));
        when(bookmarkRepository.existsByMemberAndArtist(any(), any())).thenReturn(false);
        when(bookmarkRepository.save(any())).thenReturn(testArtist);

        //when
        BookmarkRegisterDTO.BookmarkRegisterResponseDTO response = bookmarkService.register("test123", dto);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);

        verify(memberRepository, times(1)).findByUsername(any());
        verify(artistRepository, times(1)).findById(any());
        verify(bookmarkRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("존재하지 않는 회원이 찜을 등록하면 정해진 예외를 반환한다")
    public void register_memberNotFound(){
        //given
        BookmarkRegisterDTO.BookmarkRegisterRequestDTO dto = BookmarkRegisterDTO.BookmarkRegisterRequestDTO.builder()
                .artistId(1L)
                .build();

        String error_username = "error";
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> bookmarkService.register(error_username,dto))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("찜 가능 횟수가 부족한 사용자가 찜을 등록하면 정해진 예외를 반환한다")
    public void register_bookmarkAccountNotFound(){
        //given
        BookmarkRegisterDTO.BookmarkRegisterRequestDTO dto = BookmarkRegisterDTO.BookmarkRegisterRequestDTO.builder()
                .artistId(1L)
                .build();

        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));

        // when & then
        assertThatThrownBy(() -> bookmarkService.register("test123",dto))
                .isInstanceOf(BookmarkHandler.class)
                .hasFieldOrPropertyWithValue("code", BOOKMARK_ACCOUNT_ZERO);
    }


    @Test
    @DisplayName("존재하지 않는 아티스트를 찜할 시 정해진 예외를 반환한다")
    public void register_ArtistNotFound(){
        //given
        Member.bookmarkAccountPlus(testMember);

        BookmarkRegisterDTO.BookmarkRegisterRequestDTO dto = BookmarkRegisterDTO.BookmarkRegisterRequestDTO.builder()
                .artistId(222222L)
                .build();

        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));

        // when & then
        assertThatThrownBy(() -> bookmarkService.register("test123",dto))
                .isInstanceOf(ArtistHandler.class)
                .hasFieldOrPropertyWithValue("code", ARTIST_NOT_FOUND);
    }


    @Test
    @DisplayName("이미 찜이 등록된 아티스트에 찜을 시도하면 정해진 예외를 반환한다")
    public void register_bookmarkExist(){
        //given
        Member.bookmarkAccountPlus(testMember);

        BookmarkRegisterDTO.BookmarkRegisterRequestDTO dto = BookmarkRegisterDTO.BookmarkRegisterRequestDTO.builder()
                .artistId(1L)
                .build();

        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(bookmarkRepository.existsByMemberAndArtist(any(), any())).thenReturn(true);
        when(artistRepository.findById(any())).thenReturn(Optional.of(testArtist));

        // when & then
        assertThatThrownBy(() -> bookmarkService.register("test123",dto))
                .isInstanceOf(BookmarkHandler.class)
                .hasFieldOrPropertyWithValue("code", BOOKMARK_EXIST);
    }


    @Test
    @DisplayName("subscribe() 를 호출하면 구독권이 정해진 만큼 증가한다")
    public void subscribe_success(){
        //given
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));

        //when
        bookmarkService.subscribe("test123");

        //then
        assertThat(testMember.getBookmarkAccount()).isEqualTo(3);
    }


    @Test
    @DisplayName("존재하지 않는 username으로 구독권을 구매하면 정해진 예외를 반환한다")
    public void subscribe_fail(){
        //given
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> bookmarkService.subscribe("test123"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("getArtist()를 이용하여 찜한 아티스트를 조회 할 수 있다")
    public void getArtist_success(){
        //given
        Bookmark testBookmark = Bookmark.builder()
                .artist(testArtist)
                .member(testMember)
                .build();

        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(bookmarkRepository.findByMember(any())).thenReturn(List.of(testBookmark));

        //when
        List<BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO> response = bookmarkService.getArtist("test123");

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getArtistName()).isEqualTo("test_artist");
        assertThat(response.get(0).getGenre()).isEqualTo("A");
        assertThat(response.get(0).getArtistProfileImage()).isEqualTo("test_profile_image");
    }


    @Test
    @DisplayName("존재하지 않는 회원의 찜한 Artist를 조회하면 정해진 예외를 반환한다")
    public void getArtist_MemberNotFound(){
        //given
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> bookmarkService.getArtist("test123"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("조회할 Artist가 없으면 정해진 예외를 반환한다")
    public void getArtist_ArtistNotFound(){
        //given
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(bookmarkRepository.findByMember(any())).thenReturn(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> bookmarkService.getArtist("test123"))
                .isInstanceOf(BookmarkHandler.class)
                .hasFieldOrPropertyWithValue("code", BOOKMARK_NOT_EXIST);
    }


    @Test
    @DisplayName("bookmarkAlarm()을 이용하여 알림을 보낼 수 있다")
    public void bookmarkAlarm_success() throws MessagingException {
        //given
        String toEmail = "user@example.com";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //when
        bookmarkService.bookmarkAlarm(toEmail);

        //then
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }


    @Test
    @DisplayName("이메일 형식에 맞지 않으면 정해진 예외를 반환한다")
    public void bookmarkAlarm_pattern(){
        //given
        String toEmail = "error";

        //when & then
        assertThatThrownBy(() -> bookmarkService.bookmarkAlarm(toEmail))
                .isInstanceOf(MailHandler.class)
                .hasFieldOrPropertyWithValue("code", MAIL_NOT_VALID);
    }


    @Test
    @DisplayName("이메일이 입력되지 않으면 정해진 예외를 반환한다")
    public void bookmarkAlarm_email_null(){
        //given
        String toEmail = "";

        //when & then
        assertThatThrownBy(() -> bookmarkService.bookmarkAlarm(toEmail))
                .isInstanceOf(MailHandler.class)
                .hasFieldOrPropertyWithValue("code", MAIL_NOT_VALID);
    }
}