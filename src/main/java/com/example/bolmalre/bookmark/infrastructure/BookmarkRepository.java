package com.example.bolmalre.bookmark.infrastructure;


import com.example.bolmalre.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
