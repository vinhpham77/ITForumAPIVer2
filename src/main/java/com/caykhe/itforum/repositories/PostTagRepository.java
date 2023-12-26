package com.caykhe.itforum.repositories;

import com.caykhe.itforum.dtos.TagCount;
import com.caykhe.itforum.models.PostTag;
import com.caykhe.itforum.models.PostTagId;
import com.caykhe.itforum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    @Query("SELECT new com.caykhe.itforum.dtos.TagCount(t.name, COUNT(t.id)) " +
            "FROM PostTag pt " +
            "JOIN pt.post p " +
            "JOIN pt.tag t " +
            "WHERE p.createdBy.username = :username " +
            "GROUP BY t.id")
    List<TagCount> countTagsByUsername(@Param("username") String username);

    int countByPost_CreatedByAndTag_Name(User user, String tagName);
}