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

    Page<Series> findByIdIn(List<Integer> ids, Pageable pageable);

    int countByCreatedBy(User user);
}