package com.caykhe.itforum.models;

import jakarta.persistence.*;
import lombok.*;

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