package com.example.bolmalre.bookmark.web.port;

import com.example.bolmalre.bookmark.web.dto.BookmarkRegisterDTO;

public interface BookmarkService {
    BookmarkRegisterDTO.BookmarkRegisterResponseDTO register(String username,
                                                             BookmarkRegisterDTO.BookmarkRegisterRequestDTO request);

    void subscribe(String username);
}
