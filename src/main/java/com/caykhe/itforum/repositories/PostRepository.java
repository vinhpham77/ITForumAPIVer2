package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByCreatedByUsername(String createdBy, Pageable pageable);
    Page<Post> findByCreatedByUsernameAndIsPrivateFalse(String createdBy, Pageable pageable);
    Page<Post> findByCreatedByUsernameAndTagsName(String createdBy, String tag, Pageable pageable);
    Page<Post> findByCreatedByUsernameAndTagsNameAndIsPrivateFalse(String createdBy, String tag, Pageable pageable);
}