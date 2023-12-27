package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Post;
import com.caykhe.itforum.models.Series;
import com.caykhe.itforum.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Integer> {

    Page<Series> findByCreatedByUsername(String username, Pageable pageable);

    Page<Series> findByCreatedByUsernameAndIsPrivateFalse(String username, Pageable pageable);

    Page<Series> findByIsPrivateFalse(Pageable pageable);

    @Query("SELECT s FROM Series s JOIN s.createdBy u WHERE u.username IN :usernames")
    Page<Series> findByCreatedByInAndIsPrivateFalse(@Param("usernames") List<String> usernames, Pageable pageable);

    @Query("SELECT s FROM Series s WHERE s.title LIKE %:title% AND s.isPrivate = false")
    Page<Series> findByTitleContainingAndIsPrivateFalse(@Param("title") String title, Pageable pageable);

    @Query("SELECT s FROM Series s WHERE s.createdBy.displayName LIKE %:displayName% AND s.isPrivate = false")
    Page<Series> findByCreatedBy_DisplayNameContainingAndIsPrivateFalse(@Param("displayName") String displayName, Pageable pageable);

    @Query("SELECT s FROM Series s WHERE s.content LIKE %:content% AND s.isPrivate = false")
    Page<Series> findByContentContainingAndIsPrivateFalse(@Param("content") String content, Pageable pageable);

    @Query("SELECT s FROM Series s JOIN s.createdBy u WHERE (s.title LIKE %:searchContent% OR u.displayName LIKE %:searchContent% OR s.content LIKE %:searchContent%) AND s.isPrivate = false")
    Page<Series> findByTitleOrDisplayNameOrContentContainingAndIsPrivateFalse(@Param("searchContent") String searchContent,
                                                                                      Pageable pageable);

    Page<Series> findByIdIn(List<Integer> ids, Pageable pageable);

    int countByCreatedBy(User user);
}