package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Bookmark;
import com.caykhe.itforum.models.BookmarkPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkPostRepository extends JpaRepository<BookmarkPost, Integer> {

    List<BookmarkPost> findByBookmarkAndTypeFalse(Bookmark bookmark);

    List<BookmarkPost> findByBookmarkAndTypeTrue(Bookmark bookmark);
}
