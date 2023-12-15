package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SubCommentAggregate {

    private String id;

    private String username;

    private String content;

    private Date updatedAt;

    private int left;

    private int right;

    private UserResponse user;
}
