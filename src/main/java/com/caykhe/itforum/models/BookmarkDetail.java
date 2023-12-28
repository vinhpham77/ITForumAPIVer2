package com.caykhe.itforum.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "bookmark_details")
public class BookmarkDetail {
    @EmbeddedId
    private BookmarkDetailId id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="bookmark_id", referencedColumnName="id", insertable=false, updatable=false)
    private Bookmark bookmark;

    @NotNull
    @Column(name = "target_id", nullable = false, insertable=false, updatable=false)
    private Integer targetId;

    @NotNull
    @Column(name = "type", nullable = false, insertable=false, updatable=false)
    private Boolean type = false;
}