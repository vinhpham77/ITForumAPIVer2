package com.caykhe.itforum.utils;

import com.caykhe.itforum.dtos.SeriesDto;
import com.caykhe.itforum.models.Post;
import com.caykhe.itforum.models.Series;
import com.caykhe.itforum.models.SeriesPost;
import com.caykhe.itforum.repositories.SeriesPostRepository;

import java.util.List;

public class ConverterUtils {
    public static SeriesDto convertSeriesDto(Series series, SeriesPostRepository seriesPostRepository) {
        List<Integer> postIds = seriesPostRepository.findAllBySeriesId(series.getId())
                .stream().map(SeriesPost::getPost)
                .map(Post::getId)
                .toList();

        return SeriesDto.builder()
                .id(series.getId())
                .title(series.getTitle())
                .content(series.getContent())
                .postIds(postIds)
                .isPrivate(series.getIsPrivate())
                .createdBy(series.getCreatedBy())
                .updatedAt(series.getUpdatedAt())
                .score(series.getScore())
                .commentCount(series.getCommentCount())
                .build();
    }
}
