package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.PostDto;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.dtos.UserLatestPageable;
import com.caykhe.itforum.models.Notification;
import com.caykhe.itforum.models.Post;
import com.caykhe.itforum.models.Tag;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.repositories.NotificationRepository;
import com.caykhe.itforum.repositories.PostRepository;
import com.caykhe.itforum.repositories.UserRepository;
import com.caykhe.itforum.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserRepository userRepository;
    private final FollowService followService;
    private final CommentService commentService;

    public Post get(Integer id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("Bài viết không tồn tại", HttpStatus.NOT_FOUND));

        String requester = SecurityContextHolder.getContext().getAuthentication().getName();
        String createdBy = post.getCreatedBy().getUsername();

        if (post.getIsPrivate() && !createdBy.equals(requester)) {
            throw new ApiException("Bài viết tạm thời không có sẵn với mọi người", HttpStatus.FORBIDDEN);
        }

        return post;
    }

    @Transactional
    public ResultCount<Post> getByUser(String createdBy, String tag, Integer page, Integer size) {
        Page<Post> postPage;
        UserLatestPageable result = PaginationUtils.getUserLatestPageable(page, size);

        if (result.requester().equals(createdBy)) {
            postPage = (tag.isBlank()) ? postRepository.findByCreatedByUsername(createdBy, result.pageable())
                    : postRepository.findByCreatedByUsernameAndTagsName(createdBy, tag, result.pageable());
        } else {
            postPage = (tag.isBlank()) ? postRepository.findByCreatedByUsernameAndIsPrivateFalse(createdBy, result.pageable())
                    : postRepository.findByCreatedByUsernameAndTagsNameAndIsPrivateFalse(createdBy, tag, result.pageable());
        }

        List<Post> posts = postPage.toList();
        long count = postPage.getTotalElements();

        return new ResultCount<>(posts, count);
    }
    @Autowired
    private NotificationRepository notificationRepository;
    @Transactional
    public Post create(PostDto postDto) {
        var requester = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(requester)
                .orElseThrow(() -> new ApiException("Người dùng @" + requester + " không tồn tại", HttpStatus.NOT_FOUND));

        Set<Tag> tags = postDto.getTags().stream()
                .map(tagService::findByName)
                .collect(Collectors.toSet());

        Post post = Post
                .builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .isPrivate(postDto.getIsPrivate())
                .createdBy(user)
                .tags(tags)
                .build();

        try {
            post = postRepository.save(post);
            commentService.create(post.getId(), false);
            // Tạo và lưu thông báo
            Notification notification = new Notification();
            notification.setUsername(user.getUsername()); // Sửa lại thành username
            notification.setContent("@" + requester + " đã tạo một bài viết mới: " + post.getTitle());
            notification.setCreatedAt(Instant.now());
            notification.setRead(false);
            notification.setType("post");
            notification.setTargetId(post.getId());
            notificationRepository.save(notification);

            return post;
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra khi tạo bài viết. Vui lòng thử lại!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public Post update(Integer id, PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("Bài viết không tồn tại", HttpStatus.NOT_FOUND));

        String createdBy = post.getCreatedBy().getUsername();
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!createdBy.equals(requester)) {
            throw new ApiException("Bạn không có quyền chỉnh sửa bài viết này", HttpStatus.FORBIDDEN);
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setIsPrivate(postDto.getIsPrivate());
        post.setUpdatedAt(new Date().toInstant());

        List<Tag> tags = postDto.getTags().stream()
                .map(tagService::findByName)
                .toList();

        post.setTags(new HashSet<>(tags));

        try {
            return postRepository.save(post);
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra khi cập nhật bài viết. Vui lòng thử lại!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void delete(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("Bài viết không tồn tại", HttpStatus.NOT_FOUND));

        String createdBy = post.getCreatedBy().getUsername();
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!createdBy.equals(requester)) {
            throw new ApiException("Bạn không có quyền xóa bài viết này", HttpStatus.FORBIDDEN);
        }

        try {
            commentService.deleteComment(post.getId(), false);
            postRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra khi xóa bài viết. Vui lòng thử lại!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResultCount<Post> getPosts(Integer page, Integer size, String tag) {
        Page<Post> postPage;
        Pageable pageable = (page == null || size == null || page < 1 || size < 1)
                ? Pageable.unpaged()
                : PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        postPage = tag.isBlank() ? postRepository.findByIsPrivateFalse(pageable)
                : postRepository.findByTagsNameAndIsPrivateFalse(tag, pageable);

        List<Post> posts = postPage.toList();
        long count = postPage.getTotalElements();

        return new ResultCount<>(posts, count);
    }

    @Transactional
    public ResultCount<Post> getPostsFollow(Integer page, Integer size, String tag) {
        Page<Post> postPage;
        Pageable pageable = (page == null || size == null || page < 1 || size < 1)
                ? Pageable.unpaged()
                : PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        List<String> usernames = followService.getFollowedByFollower();
        postPage = tag.isBlank() ? postRepository.findByCreatedByInAndIsPrivateFalse(usernames, pageable)
                : postRepository.findByCreatedByInAndTagsNameAndIsPrivateFalse(usernames, tag, pageable);

        List<Post> posts = postPage.toList();
        long count = postPage.getTotalElements();

        return new ResultCount<>(posts, count);
    }

    @Transactional
    public ResultCount<Post> search(String fieldSearch, String searchContent, String sort, String sortField, Integer page, Integer limit) {
        Page<Post> postPage;
        sortField = sortField.isEmpty() ? "updatedAt" : sortField;
        Pageable pageable = (page == null || limit == null || page < 1 || limit < 1)
                ? Pageable.unpaged()
                : PageRequest.of(page - 1, limit, Sort.by("ASC".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));

        postPage = switch (fieldSearch) {
            case "title" -> postRepository.findByTitleContainingAndIsPrivateFalse(searchContent, pageable);
            case "content" -> postRepository.findByContentContainingAndIsPrivateFalse(searchContent, pageable);
            case "username" ->
                    postRepository.findByCreatedBy_DisplayNameContainingAndIsPrivateFalse(searchContent, pageable);
            case "tag" -> postRepository.findByTagsNameContainingAndIsPrivateFalse(searchContent, pageable);
            case "" ->
                    postRepository.findByTitleOrDisplayNameOrTagsNameOrContentContainingAndIsPrivateFalse(searchContent, pageable);
            default ->
                    throw new ApiException("Lỗi! Không thể tìm kiếm theo trường " + fieldSearch, HttpStatus.BAD_REQUEST);
        };
        try {
            List<Post> posts = postPage.toList();
            long count = postPage.getTotalElements();

            return new ResultCount<>(posts, count);
        } catch (Exception e) {
            throw new ApiException("Lỗi! không thể tim kiếm", HttpStatus.FORBIDDEN);
        }
    }

    public List<Post> postsByTheSameAuthorsExcludingCurrent(String authorName, Integer currentPostId) {
        Optional<User> author=userRepository.findByUsername(authorName);
        return postRepository.findByCreatedByAndIdNot(author.get(), currentPostId);
    }
    public int countPostCreateby(String username){
        Optional<User> user=userRepository.findByUsername(username);
        if (user.isPresent()){
            return postRepository.countByCreatedBy(user.get());
        }
        else{
            throw new ApiException("User không tồn tại",HttpStatus.NOT_FOUND);
        }
    }
    public Post upDateScore(Integer id, int score) {
        {
            Optional<Post> postOptional = postRepository.findById(id);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                post.setScore(score);
                return postRepository.save(post);
            } else {
                throw new ApiException("Không tìm thấy post cần vote", HttpStatus.NOT_FOUND);
            }
        }
    }



}