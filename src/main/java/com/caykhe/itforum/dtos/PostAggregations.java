package com.caykhe.itforum.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostAggregations {

    private String id;

    private String title;

    private String content;

    private List<String> tags;

    private int score;

    private boolean isPrivate;
    
    private int commentCount;

    private Date updatedAt;

    private UserResponse user;
}
