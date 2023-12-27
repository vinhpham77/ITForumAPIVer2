package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findByTargetIdAndType(Integer targetId, boolean type);
}