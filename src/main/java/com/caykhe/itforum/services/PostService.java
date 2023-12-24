package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.PostDto;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.models.*;
import com.caykhe.itforum.repositories.PostRepository;
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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserRepository userRepository;

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
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = (page == null || size == null || page < 0 || size <= 0)
                ? Pageable.unpaged()
                : PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        if (requester.equals(createdBy)) {
            postPage = (tag.isBlank()) ? postRepository.findByCreatedByUsername(createdBy, pageable)
                    : postRepository.findByCreatedByUsernameAndTagsName(createdBy, tag, pageable);
        } else {
            postPage = (tag.isBlank()) ? postRepository.findByCreatedByUsernameAndIsPrivateFalse(createdBy, pageable)
                    : postRepository.findByCreatedByUsernameAndTagsNameAndIsPrivateFalse(createdBy, tag, pageable);
        }

        List<Post> posts = postPage.toList();
        long count = postPage.getTotalElements();

        return new ResultCount<>(posts, count);
    }

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
            return postRepository.save(post);
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
            postRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra khi xóa bài viết. Vui lòng thử lại!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}