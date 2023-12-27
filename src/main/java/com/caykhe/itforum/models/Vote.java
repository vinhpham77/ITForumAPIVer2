package com.caykhe.itforum.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VoteId.class)
@Table(name = "votes",schema = "ITForum")

public class Vote {

    @Id
    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @Id
    @Column(name = "type", nullable = false)
    private Boolean type = false;

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "username", nullable = false, referencedColumnName = "username")
    private User user;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}