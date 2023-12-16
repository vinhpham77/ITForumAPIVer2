package com.caykhe.itforum.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookmark_posts")
public class BookmarkPost {
    @EmbeddedId
    private BookmarkPostId id;

}