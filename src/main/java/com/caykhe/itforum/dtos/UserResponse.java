package com.caykhe.itforum.dtos;

import com.caykhe.itforum.models.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserResponse {
    private String id;

    private String username;

    private String email;

    private Boolean gender = null;

    private Date birthdate;

    private String avatarUrl;

    private String bio;

    private Role role;

    private String displayName;
}
