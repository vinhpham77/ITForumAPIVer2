package com.caykhe.itforum.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Data
@Builder
public class User implements UserDetails {
    @Id
    private String id;

//    @Indexed(unique = true)
    private String username;

    @NotNull
    private String password;

//    @Indexed(unique = true)
    private String email;

    @Null
    @Builder.Default
    private Boolean gender = null;

    private Date birthdate;

    private String avatarUrl;

    private String bio;

    @NotNull
    private Role role;

    private String displayName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}