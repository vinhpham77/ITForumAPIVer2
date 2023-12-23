package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Integer> {

    Page<Series> findByCreatedByUsername(String username, Pageable pageable);

    Page<Series> findByCreatedByUsernameAndIsPrivateFalse(String username, Pageable pageable);

    long countByCreatedByUsername(String createdBy);

    long countByCreatedByUsernameAndIsPrivateFalse(String username);
}