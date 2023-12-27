package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.dtos.SeriesDto;
import com.caykhe.itforum.dtos.UserLatestPageable;
import com.caykhe.itforum.models.*;
import com.caykhe.itforum.repositories.SeriesPostRepository;
import com.caykhe.itforum.repositories.SeriesRepository;
import com.caykhe.itforum.repositories.UserRepository;
import com.caykhe.itforum.utils.ConverterUtils;
import com.caykhe.itforum.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    private final SeriesPostRepository seriesPostRepository;
    private final CommentService commentService;
    private final FollowService followService;

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
        UserLatestPageable result = PaginationUtils.getUserLatestPageable(page, size);

        if (result.requester().equals(createdBy)) {
            seriesPage = seriesRepository.findByCreatedByUsername(createdBy, result.pageable());
        } else {
            seriesPage = seriesRepository.findByCreatedByUsernameAndIsPrivateFalse(createdBy, result.pageable());
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
            series = seriesRepository.save(series);
            commentService.create(series.getId(), true);
            return series;
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
            commentService.deleteComment(series.getId(), true);
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
        return ConverterUtils.convertSeriesDto(series, seriesPostRepository);
    }


    @Transactional
    public ResultCount<SeriesDto> getSeries(Integer page, Integer limit) {
        Page<Series> seriesPage;
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

    @Transactional
    public ResultCount<SeriesDto> getSeriesFollow(Integer page, Integer size) {
        Page<Series> seriesPage;
        Pageable pageable = (page == null || size == null || page < 0 || size <= 0)
                ? Pageable.unpaged()
                : PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        List<String> usernames = followService.getFollowedByFollower();
        seriesPage = seriesRepository.findByCreatedByInAndIsPrivateFalse(usernames, pageable);

        List<SeriesDto> seriesDtos = seriesPage.getContent().stream()
                .map(this::convertToDto)
                .toList();

        long count = seriesPage.getTotalElements();

        return new ResultCount<>(seriesDtos, count);
    }
    public List<Post> getListPost(Integer seriesId) {
        Optional<Series> seriesOptional = seriesRepository.findById(seriesId);
        List<Post> postList = new ArrayList<>();
        if (seriesOptional.isPresent()) {
            List<SeriesPost> seriesPostList = seriesPostRepository.findBySeriesId(seriesId);
            for (SeriesPost seriesPost : seriesPostList) {
                postList.add(seriesPost.getPost());
                System.out.println(seriesPost.getPost().getTitle());
            }
            return postList;
        }else{
            throw new ApiException("Không tìm thấy series",HttpStatus.NOT_FOUND);
        }

    }

}
