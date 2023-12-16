package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.PostDto;
import com.caykhe.itforum.models.Post;
import com.caykhe.itforum.models.PostTag;
import com.caykhe.itforum.models.Tag;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.repositories.PostRepository;
import com.caykhe.itforum.repositories.PostTagRepository;
import com.caykhe.itforum.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserRepository userRepository;
    private final PostTagRepository postTagRepository;

    public Post get(Integer id) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        var post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("Bài viết không tồn tại", HttpStatus.NOT_FOUND));

        if (post.getIsPrivate() && !post.getCreatedBy().getUsername().equals(username)) {
            throw new ApiException("Bài viết tạm thời không có sẵn với mọi người", HttpStatus.FORBIDDEN);
        }

        return post;
    }

    @Transactional
    public Post create(PostDto postDto) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Người dùng @" + username + " không tồn tại", HttpStatus.NOT_FOUND));

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

        Post savedPost = postRepository.save(post);

        List<PostTag> postTags = tags.stream()
                .map(tag -> PostTag.builder().post(savedPost).tag(tag).build())
                .toList();

        postTagRepository.saveAll(postTags);

        return savedPost;
    }
}