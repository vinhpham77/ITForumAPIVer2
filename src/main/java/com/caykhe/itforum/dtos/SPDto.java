package com.caykhe.itforum.dtos;

import com.caykhe.itforum.models.Post;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class SPDto {

    private String id;
    private String title;
    private String content;
    private List<String> postIds;
    private int score;
    private boolean isPrivate;
    private int commentCount;
    private String createdBy;
    private Date updatedAt;
    private List<Post> posts;
    private UserResponse user;

}
