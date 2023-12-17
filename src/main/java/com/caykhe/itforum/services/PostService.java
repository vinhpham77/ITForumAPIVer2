package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.PostDto;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.models.*;
import com.caykhe.itforum.repositories.PostRepository;
import com.caykhe.itforum.repositories.PostTagRepository;
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
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserRepository userRepository;
    private final PostTagRepository postTagRepository;

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
    
    public PostDto getDto(Integer id) {
        return convertToDto(get(id));
    }
    
    @Transactional
    public ResultCount<PostDto> getByUser(String createdBy, Integer page, Integer size) {
        long count;
        Page<Post> postPage;
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = (page == null || size == null || page < 0 || size <= 0)
                ? Pageable.unpaged()
                : PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        if (requester.equals(createdBy)) {
            postPage = postRepository.findByCreatedByUsername(createdBy, pageable);
            count = postRepository.countByCreatedByUsername(createdBy);
        } else {
            postPage = postRepository.findByCreatedByUsernameAndIsPrivateFalse(createdBy, pageable);
            count = postRepository.countByCreatedByUsernameAndIsPrivateFalse(createdBy);
        }

        List<PostDto> postDtos = postPage.getContent().stream()
                .map(this::convertToDto)
                .toList();

        return new ResultCount<>(postDtos, count);
    }

    @Transactional
    public Post create(PostDto postDto) {
        var requester = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(requester)
                .orElseThrow(() -> new ApiException("Người dùng @" + requester + " không tồn tại", HttpStatus.NOT_FOUND));

        Post post = Post
                .builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .isPrivate(postDto.getIsPrivate())
                .createdBy(user)
                .build();

        List<Tag> tags = postDto.getTags().stream()
                .map(tagService::findByName)
                .toList();

        try {
            Post savedPost = postRepository.save(post);
            List<PostTag> postTags = createPostTags(tags, savedPost);
            postTagRepository.saveAll(postTags);

            return savedPost;
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

        try {
            Post savedPost = postRepository.save(post);
            List<PostTag> newPostTags = createPostTags(tags, savedPost);
            List<PostTag> currentPostTags = postTagRepository.findAllByPostId(id);

            List<PostTag> postTagsToDelete = new ArrayList<>(currentPostTags);
            postTagsToDelete.removeAll(newPostTags);

            List<PostTag> postTagsToAdd = new ArrayList<>(newPostTags);
            postTagsToAdd.removeAll(currentPostTags);

            postTagRepository.deleteAll(postTagsToDelete);
            postTagRepository.saveAll(postTagsToAdd);

            return savedPost;
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

    private static List<PostTag> createPostTags(List<Tag> tags, Post savedPost) {
        return tags.stream()
                .map(tag -> PostTag
                        .builder()
                        .id(PostTagId.builder().tagId(tag.getId()).postId(savedPost.getId()).build())
                        .post(savedPost)
                        .tag(tag)
                        .build()
                ).toList();
    }

    private PostDto convertToDto(Post post) {
        List<String> tags = postTagRepository.findAllByPostId(post.getId())
                .stream().map(PostTag::getTag)
                .map(Tag::getName)
                .toList();

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .tags(tags)
                .isPrivate(post.getIsPrivate())
                .createdBy(post.getCreatedBy())
                .updatedAt(post.getUpdatedAt())
                .score(post.getScore())
                .commentCount(post.getCommentCount())
                .build();
    }

}