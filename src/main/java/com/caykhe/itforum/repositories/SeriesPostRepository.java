package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.SeriesPost;
import com.caykhe.itforum.models.SeriesPostId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesPostRepository extends JpaRepository<SeriesPost, SeriesPostId> {
    List<SeriesPost> findAllBySeriesId(Integer id);
    List<SeriesPost> findBySeriesId(Integer id);
}