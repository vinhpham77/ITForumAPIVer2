package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.BookmarkPostRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkPostRepository bookmarkPostRepository;
    private final PostRepository postRepository;
    private final SeriesRepository seriesRepository;
    private final SeriesPostRepository seriesPostRepository;
    private final UserRepository userRepository;

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

    public Bookmark createBookmark(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Bookmark bookmark = new Bookmark();
        user.ifPresent(bookmark::setUsername);
        return bookmarkRepository.save(bookmark);
    }

    public BookmarkPost addBookmarkPost(Bookmark bookmark, Integer targetId, Boolean type) {
        BookmarkPostId id = new BookmarkPostId();
        id.setBookmarkId(bookmark.getId());
        id.setTargetId(targetId);
        id.setType(type);

        BookmarkPost bookmarkPost = new BookmarkPost();
        bookmarkPost.setId(id);
        bookmarkPost.setBookmark(bookmark);
        bookmarkPost.setTargetId(targetId);
        bookmarkPost.setType(type);

        return bookmarkPostRepository.save(bookmarkPost);
    }


    public BookmarkPost bookmark(String username, BookmarkPostRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByUsername(userOptional.get());
            Bookmark bookmark;
            bookmark = bookmarkOptional.orElseGet(() -> createBookmark(username));
            return addBookmarkPost(bookmark, request.getTargetId(), request.getType());
        } else {
            throw new ApiException("User not found with username: " + username, HttpStatus.NOT_FOUND);
        }
    }

    public Optional<Bookmark> getBookmarkById(Integer id) {
        return bookmarkRepository.findById(id);
    }

    public void unBookmark(String username, BookmarkPostRequest bookmarkPostRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found with username: " + username, HttpStatus.NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUsername(user)
                .orElseThrow(() -> new ApiException("Bookmark not found for user: " + username, HttpStatus.NOT_FOUND));

        Integer targetId = bookmarkPostRequest.getTargetId();
        Boolean type = bookmarkPostRequest.getType();

        BookmarkPost bookmarkPost = bookmarkPostRepository.findByTargetIdAndAndType(targetId, type)
                .orElseThrow(() -> new ApiException("BookmarkPost not found for targetId: " + targetId + " and type: " + type, HttpStatus.NOT_FOUND));

        bookmarkPostRepository.delete(bookmarkPost);

        if (bookmarkPostRepository.findByBookmark(bookmark).isEmpty()) {
            bookmarkRepository.delete(bookmark);
        }
    }

    public Boolean isBookmark(String username, BookmarkPostRequest bookmarkPostRequest) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Optional<Bookmark> bookmark = bookmarkRepository.findByUsername(user.get());
            if (bookmark.isEmpty()) {
                return false;
            } else {
                Integer targetId = bookmarkPostRequest.getTargetId();
                Boolean type = bookmarkPostRequest.getType();

                Optional<BookmarkPost> bookmarkPost = bookmarkPostRepository.findByTargetIdAndAndType(targetId, type);
                return bookmarkPost.isPresent();
            }
        } else {
            return false;
        }
    }

}
