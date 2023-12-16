package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.models.Post;
import com.caykhe.itforum.repositories.PostRepository;
import com.caykhe.itforum.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public Post get(Integer id) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        var post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("Bài viết không tồn tại", HttpStatus.NOT_FOUND));

        if (post.getIsPrivate() && !post.getCreatedBy().getUsername().equals(username)) {
            throw new ApiException("Bài viết tạm thời không có sẵn với mọi người", HttpStatus.FORBIDDEN);
        }

        return post;
    }
}