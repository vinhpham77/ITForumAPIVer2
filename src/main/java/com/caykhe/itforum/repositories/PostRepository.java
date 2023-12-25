package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Post;
import com.caykhe.itforum.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByCreatedByUsername(String createdBy, Pageable pageable);

    Page<Post> findByCreatedByUsernameAndIsPrivateFalse(String createdBy, Pageable pageable);

    Page<Post> findByCreatedByUsernameAndTagsName(String createdBy, String tag, Pageable pageable);

    Page<Post> findByCreatedByUsernameAndTagsNameAndIsPrivateFalse(String createdBy, String tag, Pageable pageable);

    Page<Post> findByIsPrivateFalse(Pageable pageable);

    Page<Post> findByTagsNameAndIsPrivateFalse(String tag, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.createdBy u WHERE u.username IN :usernames")
    Page<Post> findByCreatedByInAndIsPrivateFalse(@Param("usernames") List<String> usernames, Pageable pageable);

    int countByCreatedBy(User createdBy);
}