package com.caykhe.itforum.dtos;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserMetrics {
    @Id
    private String id;
    private String username;
    private String email;
    private String displayName;
    private String avatarUrl;
    private Role role;
    private int postCount;
    private int seriesCount;
    private int followerCount;
}
