package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.dtos.SeriesDto;
import com.caykhe.itforum.dtos.SeriesUser;
import com.caykhe.itforum.models.*;
import com.caykhe.itforum.repositories.SeriesPostRepository;
import com.caykhe.itforum.repositories.SeriesRepository;
import com.caykhe.itforum.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    private final SeriesPostRepository seriesPostRepository;

    public Series get(Integer id) {
        var series = seriesRepository.findById(id)
                .orElseThrow(() -> new ApiException("Series không tồn tại", HttpStatus.NOT_FOUND));

        String requester = SecurityContextHolder.getContext().getAuthentication().getName();
        String createdBy = series.getCreatedBy().getUsername();

        if (series.getIsPrivate() && !createdBy.equals(requester)) {
            throw new ApiException("Series tạm thời không có sẵn với mọi người", HttpStatus.FORBIDDEN);
        }

        return series;
    }

    public SeriesDto getDto(Integer id) {
        return convertToDto(get(id));
    }

    @Transactional
    public ResultCount<SeriesDto> getByUser(String createdBy, Integer page, Integer size) {
        Page<Series> seriesPage;
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = (page == null || size == null || page < 0 || size <= 0)
                ? Pageable.unpaged()
                : PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        if (requester.equals(createdBy)) {
            seriesPage = seriesRepository.findByCreatedByUsername(createdBy, pageable);
        } else {
            seriesPage = seriesRepository.findByCreatedByUsernameAndIsPrivateFalse(createdBy, pageable);
        }

        List<SeriesDto> seriesDtos = seriesPage.getContent().stream()
                .map(this::convertToDto)
                .toList();
        
        long count = seriesPage.getTotalElements();

        return new ResultCount<>(seriesDtos, count);
    }

    @Transactional
    public Series create(SeriesDto seriesDto) {
        var requester = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(requester)
                .orElseThrow(() -> new ApiException("Người dùng @" + requester + " không tồn tại", HttpStatus.NOT_FOUND));

        Series series = Series
                .builder()
                .title(seriesDto.getTitle())
                .content(seriesDto.getContent())
                .isPrivate(seriesDto.getIsPrivate())
                .createdBy(user)
                .build();

        try {
            return seriesRepository.save(series);
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra khi tạo series. Vui lòng thử lại!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public Series update(Integer id, SeriesDto seriesDto) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ApiException("Series không tồn tại", HttpStatus.NOT_FOUND));

        String createdBy = series.getCreatedBy().getUsername();
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!createdBy.equals(requester)) {
            throw new ApiException("Bạn không có quyền chỉnh sửa series này", HttpStatus.FORBIDDEN);
        }

        series.setTitle(seriesDto.getTitle());
        series.setContent(seriesDto.getContent());
        series.setIsPrivate(seriesDto.getIsPrivate());
        series.setUpdatedAt(new Date().toInstant());

        List<Post> posts = seriesDto.getPostIds().stream()
                .map(postService::get)
                .toList();

        try {
            Series savedSeries = seriesRepository.save(series);
            List<SeriesPost> newSeriesPosts = createSeriesPosts(savedSeries, posts);
            List<SeriesPost> currentSeriesPosts = seriesPostRepository.findAllBySeriesId(id);

            List<SeriesPost> seriesPostsToDelete = new ArrayList<>(currentSeriesPosts);
            seriesPostsToDelete.removeAll(newSeriesPosts);

            List<SeriesPost> seriesPostsToAdd = new ArrayList<>(newSeriesPosts);
            seriesPostsToAdd.removeAll(currentSeriesPosts);

            seriesPostRepository.deleteAll(seriesPostsToDelete);
            seriesPostRepository.saveAll(seriesPostsToAdd);

            return savedSeries;
        } catch (Exception e) {
            throw new ApiException(
                    "Có lỗi xảy ra khi cập nhật series. Vui lòng thử lại!",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional
    public void delete(Integer id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ApiException("Series không tồn tại", HttpStatus.NOT_FOUND));

        String createdBy = series.getCreatedBy().getUsername();
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!createdBy.equals(requester)) {
            throw new ApiException("Bạn không có quyền xóa series này", HttpStatus.FORBIDDEN);
        }

        try {
            seriesRepository.delete(series);
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra khi xóa series. Vui lòng thử lại!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static List<SeriesPost> createSeriesPosts(Series series, List<Post> posts) {
        return posts.stream()
                .map(post -> SeriesPost
                        .builder()
                        .id(SeriesPostId.builder().postId(post.getId()).seriesId(series.getId()).build())
                        .series(series)
                        .post(post)
                        .build()
                ).toList();
    }

    private SeriesDto convertToDto(Series series) {
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

    public ResultCount<SeriesDto> getSeries(Integer page, Integer limit) {
        Page<Series> seriesPage;
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = (page == null || limit == null || page < 0 || limit <= 0)
                ? Pageable.unpaged()
                : PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "updatedAt"));

        seriesPage = seriesRepository.findByIsPrivateFalse(pageable);

        List<SeriesDto> seriesDtos = seriesPage.getContent().stream()
                .map(this::convertToDto)
                .toList();

        long count = seriesPage.getTotalElements();

        return new ResultCount<>(seriesDtos, count);
    }
}
