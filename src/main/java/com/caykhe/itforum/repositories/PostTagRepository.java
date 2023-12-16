package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.PostTag;
import com.caykhe.itforum.models.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
}