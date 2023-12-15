package com.caykhe.itforum.dtos;

import com.caykhe.itforum.models.Authentication;
import com.caykhe.itforum.models.User;
import lombok.Builder;

@Builder
public record RecordAuthUser(Authentication authentication, User user) {
}