package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Comment;
import com.caykhe.itforum.models.CommentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDetailsRepository extends JpaRepository<CommentDetails, Integer> {

    List<CommentDetails> findByComment(Comment comment);
}
