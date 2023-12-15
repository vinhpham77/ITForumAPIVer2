package com.caykhe.itforum.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SubComment {
    private String id;
    private String username;
    private String content;
    private Date updatedAt;
    private int left;
    private int right;

    // getters and setters
}

//String id = UUID.randomUUID().toString();