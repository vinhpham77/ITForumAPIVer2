package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}