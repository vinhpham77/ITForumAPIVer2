package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowRequest {
    private String follower;
    private String followed;
}
