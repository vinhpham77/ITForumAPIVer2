package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserResponse {
    private String id;

    private String username;

    private String email;

    private Boolean gender;

    private Date birthdate;

    private String avatarUrl;

    private String bio;

    private Role role;

    private String displayName;
}
