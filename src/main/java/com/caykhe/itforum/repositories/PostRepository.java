package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByCreatedByUsername(String username, Pageable pageable);

    long countByCreatedByUsername(String username);
    Page<Post> findByCreatedByUsernameAndIsPrivateFalse(String username, Pageable pageable);
    long countByCreatedByUsernameAndIsPrivateFalse(String username);
}