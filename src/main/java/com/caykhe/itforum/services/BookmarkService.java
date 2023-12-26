package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.dtos.SeriesDto;
import com.caykhe.itforum.models.*;
import com.caykhe.itforum.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.caykhe.itforum.utils.ConverterUtils.convertSeriesDto;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkPostRepository bookmarkPostRepository;
    private final PostRepository postRepository;
    private final SeriesRepository seriesRepository;
    private final SeriesPostRepository seriesPostRepository;

    public ResultCount<Post> getPostByUserName(String createdBy, Integer page, Integer limit, String tag) {

        Bookmark bookmark = bookmarkRepository.findByUsernameUsername(createdBy)
                .orElseThrow(() -> new ApiException("Người dùng @" + createdBy + " không tồn tại", HttpStatus.NOT_FOUND));
        try {
            List<Integer> targetIds = getTargetsByBookmark(bookmark, false);

            Page<Post> postPage;
            Pageable pageable = (page == null || limit == null || page < 0 || limit <= 0)
                    ? Pageable.unpaged()
                    : PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "updatedAt"));
            postPage = tag.isBlank() ? postRepository.findByIdIn(targetIds, pageable)
                    : postRepository.findByIdInAndTagsName(targetIds, tag, pageable);
            List<Post> posts = postPage.toList();
            long count = postPage.getTotalElements();

            return new ResultCount<>(posts, count);
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra. Vui lòng thử lại sau!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResultCount<SeriesDto> getSeriesByUserName(String createdBy, Integer page, Integer limit) {

        Bookmark bookmark = bookmarkRepository.findByUsernameUsername(createdBy)
                .orElseThrow(() -> new ApiException("Người dùng @" + createdBy + " không tồn tại", HttpStatus.NOT_FOUND));
        try {
            List<Integer> targetIds = getTargetsByBookmark(bookmark, true);

            Page<Series> seriesPage;
            Pageable pageable = (page == null || limit == null || page < 0 || limit <= 0)
                    ? Pageable.unpaged()
                    : PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "updatedAt"));
            seriesPage = seriesRepository.findByIdIn(targetIds, pageable);
            List<SeriesDto> seriesDtos = seriesPage.getContent().stream()
                    .map(this::convertToDto)
                    .toList();

            long count = seriesPage.getTotalElements();

            return new ResultCount<>(seriesDtos, count);
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra. Vui lòng thử lại sau!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Integer> getTargetsByBookmark(Bookmark bookmark, boolean isSeries) {
        List<BookmarkPost> bookmarkPosts = isSeries ? bookmarkPostRepository.findByBookmarkAndTypeFalse(bookmark)
                : bookmarkPostRepository.findByBookmarkAndTypeTrue(bookmark);
        return bookmarkPosts.stream().map(BookmarkPost::getTargetId).collect(Collectors.toList());
    }

    private SeriesDto convertToDto(Series series) {
        return convertSeriesDto(series, seriesPostRepository);
    }

}
