package com.example.bolmalre.bookmark.web.port;

import com.example.bolmalre.bookmark.web.dto.BookmarkGetArtistDTO;
import com.example.bolmalre.bookmark.web.dto.BookmarkRegisterDTO;
import jakarta.mail.MessagingException;

import java.util.List;

public interface BookmarkService {
    BookmarkRegisterDTO.BookmarkRegisterResponseDTO register(String username,
                                                             BookmarkRegisterDTO.BookmarkRegisterRequestDTO request);

    void subscribe(String username);

    List<BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO> getArtist(String username);

    void sendMail(String email) throws MessagingException;
}
