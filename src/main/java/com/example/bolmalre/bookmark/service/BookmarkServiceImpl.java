package com.example.bolmalre.bookmark.service;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.infrastructure.ArtistRepository;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.infrastructure.BookmarkRepository;
import com.example.bolmalre.bookmark.web.dto.BookmarkRegisterDTO;
import com.example.bolmalre.bookmark.web.port.BookmarkService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ArtistHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.BookmarkHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ArtistRepository artistRepository;
    private final MemberRepository memberRepository;

    @Override
    public BookmarkRegisterDTO.BookmarkRegisterResponseDTO register(String username,
                                                                    BookmarkRegisterDTO.BookmarkRegisterRequestDTO request) {


        Member findMember = findMemberByUsername(username);
        authenticateBookmarkAccount(findMember);

        Artist findArtist = findArtistById(request.getArtistId());

        authenticateBookmarkExist(findMember, findArtist);

        Bookmark newBookmark = Bookmark.builder()
                .member(findMember)
                .artist(findArtist)
                .build();

        Bookmark bookmark = bookmarkRepository.save(newBookmark);

        Member.BookmarkDiscount(findMember);

        return BookmarkRegisterDTO.BookmarkRegisterResponseDTO.builder()
                .id(bookmark.getId())
                .build();
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
