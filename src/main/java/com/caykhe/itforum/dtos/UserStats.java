package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStats {
    private int id;
    private String username;
    private String email;
    private String displayName;
    private Role role;
    private int postCount;
    private int seriesCount;
    private int followerCount;
}
