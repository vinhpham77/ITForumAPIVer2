package com.caykhe.itforum.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@IdClass(FollowId.class)
@Table(name = "follows", schema = "ITForum")
public class Follow {
    @Id
    @ManyToOne
    @JoinColumn(name = "follower", referencedColumnName = "username")
    private User follower;

    @Id
    @ManyToOne
    @JoinColumn(name = "followed", referencedColumnName = "username")
    private User followed;

}