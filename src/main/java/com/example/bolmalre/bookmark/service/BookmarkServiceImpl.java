package com.example.bolmalre.bookmark.service;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.infrastructure.ArtistRepository;
import com.example.bolmalre.bookmark.converter.BookmarkConverter;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.infrastructure.BookmarkRepository;
import com.example.bolmalre.bookmark.web.dto.BookmarkGetArtistDTO;
import com.example.bolmalre.bookmark.web.dto.BookmarkRegisterDTO;
import com.example.bolmalre.bookmark.web.port.BookmarkService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ArtistHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.BookmarkHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ArtistRepository artistRepository;
    private final MemberRepository memberRepository;

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String configEmail;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public BookmarkRegisterDTO.BookmarkRegisterResponseDTO register(String username,
                                                                    BookmarkRegisterDTO.BookmarkRegisterRequestDTO request) {


        Member findMember = findMemberByUsername(username);
        authenticateBookmarkAccount(findMember);
        Artist findArtist = findArtistById(request.getArtistId());
        authenticateBookmarkExist(findMember, findArtist);

        Bookmark newBookmark = BookmarkConverter.toBookmark(findMember,findArtist);

        Bookmark bookmark = bookmarkRepository.save(newBookmark);
        Member.bookmarkAccountMinus(findMember);

        return BookmarkConverter.toBookmarkRegisterResponseDTO(bookmark);
    }


    @Override
    public void subscribe(String username){
        Member memberByUsername = findMemberByUsername(username);

        Member.bookmarkAccountPlus(memberByUsername);
    }


    @Override
    public List<BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO> getArtist(String username){

        Member findMember = findMemberByUsername(username);
        List<Bookmark> byMember = bookmarkRepository.findByMember(findMember);
        bookmarkExistValid(byMember);

        return BookmarkConverter.toBookmarkGetArtistResponseDTO(byMember);
    }


    @Override
    public void bookmarkAlarm(String email) throws MessagingException {

        if (!isValidEmail(email)) {
            throw new MailHandler(ErrorStatus.MAIL_NOT_VALID);
        }

        MimeMessage emailForm = createEmailForm(email);
        mailSender.send(emailForm);

    }


    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private String setContext() {
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("bookmark", context);
    }

    private MimeMessage createEmailForm(String email) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[볼래말래] 회원가입 이메일 2차확인 인증번호");
        message.setFrom(configEmail);
        message.setText(setContext(), "utf-8", "html");

        return message;
    }

    private static void bookmarkExistValid(List<Bookmark> byMember) {
        if(byMember.isEmpty()){
            throw new BookmarkHandler(ErrorStatus.BOOKMARK_NOT_EXIST);
        }
    }


    // 이미 찜을 하고 있는지 검증
    private void authenticateBookmarkExist(Member findMember, Artist findArtist) {
        boolean result = bookmarkRepository.existsByMemberAndArtist(findMember, findArtist);
        if (result){
            throw new BookmarkHandler(ErrorStatus.BOOKMARK_EXIST);
        }
    }


    // 찜 가능 횟수 검증
    private static void authenticateBookmarkAccount(Member findMember) {
        if (findMember.getBookmarkAccount().equals(0)){
            throw new BookmarkHandler(ErrorStatus.BOOKMARK_ACCOUNT_ZERO);
        }
    }


    private Artist findArtistById(Long aristID) {
        return artistRepository.findById(aristID)
                .orElseThrow(() -> new ArtistHandler(ErrorStatus.ARTIST_NOT_FOUND));
    }


    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
