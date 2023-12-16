package com.caykhe.itforum.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "score", nullable = false)
    private Integer score;

    @NotNull
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @NotNull
    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "created_by", nullable = false, referencedColumnName = "username")
    private User createdBy;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}