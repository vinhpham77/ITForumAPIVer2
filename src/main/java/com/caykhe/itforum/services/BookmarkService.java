package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.BookmarkDetailRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.caykhe.itforum.utils.ConverterUtils.convertSeriesDto;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkDetailRepository bookmarkDetailRepository;
    private final PostRepository postRepository;
    private final SeriesRepository seriesRepository;
    private final SeriesPostRepository seriesPostRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public ResultCount<Post> getPostByUserName(String createdBy, Integer page, Integer limit, String tag) {
        Bookmark bookmark = bookmarkRepository.findByUsernameUsername(createdBy)
                .orElseThrow(() -> new ApiException("Bạn chưa bookmark", HttpStatus.NOT_FOUND));
        try {
            List<Integer> targetIds = getTargetsByBookmark(bookmark, false);

            Page<Post> postPage;
            Pageable pageable = (page == null || limit == null || page < 1 || limit <= 1)
                    ? Pageable.unpaged()
                    : PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "updatedAt"));
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
            Pageable pageable = (page == null || limit == null || page < 1 || limit <= 1)
                    ? Pageable.unpaged()
                    : PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "updatedAt"));
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
        List<BookmarkDetail> bookmarkDetails = isSeries ? bookmarkDetailRepository.findByBookmarkAndTypeFalse(bookmark)
                : bookmarkDetailRepository.findByBookmarkAndTypeTrue(bookmark);
        return bookmarkDetails.stream().map(BookmarkDetail::getTargetId).collect(Collectors.toList());
    }

    private SeriesDto convertToDto(Series series) {
        return convertSeriesDto(series, seriesPostRepository);
    }

    public Bookmark createBookmark(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Bookmark bookmark = new Bookmark();
        user.ifPresent(bookmark::setUsername);
        return bookmarkRepository.save(bookmark);
    }

    public BookmarkDetail addBookmarkDetail(Bookmark bookmark, Integer targetId, Boolean type) {
        BookmarkDetailId id = new BookmarkDetailId();
        id.setBookmarkId(bookmark.getId());
        id.setTargetId(targetId);
        id.setType(type);

        BookmarkDetail bookmarkPost = new BookmarkDetail();
        bookmarkPost.setId(id);
        bookmarkPost.setBookmark(bookmark);
        bookmarkPost.setTargetId(targetId);
        bookmarkPost.setType(type);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Tạo và lưu thông báo
        Notification notification = new Notification();
        notification.setUsername(user.getUsername()); // Sửa lại thành username
        notification.setContent("@" + user.getUsername() + " đã bookmark bài viết cua bạn: " + bookmarkPost.getBookmark());
        notification.setCreatedAt(Instant.now());
        notification.setRead(false);
        notification.setType("bookmark");
        notification.setTargetId(targetId);
        notificationRepository.save(notification);
        return bookmarkDetailRepository.save(bookmarkPost);

    }
    
    public BookmarkDetail bookmark(String username, BookmarkDetailRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByUsername(userOptional.get());
            Bookmark bookmark;
            bookmark = bookmarkOptional.orElseGet(() -> createBookmark(username));
            return addBookmarkDetail(bookmark, request.getTargetId(), request.getType());
        } else {
            throw new ApiException("User not found with username: " + username, HttpStatus.NOT_FOUND);
        }
    }

    public Optional<Bookmark> getBookmarkById(Integer id) {
        return bookmarkRepository.findById(id);
    }

    public void unBookmark(String username, BookmarkDetailRequest bookmarkPostRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found with username: " + username, HttpStatus.NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUsername(user)
                .orElseThrow(() -> new ApiException("Bookmark not found for user: " + username, HttpStatus.NOT_FOUND));

        Integer targetId = bookmarkPostRequest.getTargetId();
        Boolean type = bookmarkPostRequest.getType();

        BookmarkDetail bookmarkDetail = bookmarkDetailRepository.findByTargetIdAndAndType(targetId, type)
                .orElseThrow(() -> new ApiException("BookmarkDetail not found for targetId: " + targetId + " and type: " + type, HttpStatus.NOT_FOUND));

        bookmarkDetailRepository.delete(bookmarkDetail);

        if (bookmarkDetailRepository.findByBookmark(bookmark).isEmpty()) {
            bookmarkRepository.delete(bookmark);
        }
    }

    public Boolean isBookmark(String username, BookmarkDetailRequest bookmarkPostRequest) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Optional<Bookmark> bookmark = bookmarkRepository.findByUsername(user.get());
            if (bookmark.isEmpty()) {
                return false;
            } else {
                Integer targetId = bookmarkPostRequest.getTargetId();
                Boolean type = bookmarkPostRequest.getType();

                Optional<BookmarkDetail> bookmarkPost = bookmarkDetailRepository.findByTargetIdAndAndType(targetId, type);
                return bookmarkPost.isPresent();
            }
        } else {
            return false;
        }
    }

}
