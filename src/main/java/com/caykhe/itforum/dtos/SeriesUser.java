package com.caykhe.itforum.dtos;

import com.caykhe.itforum.models.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SeriesUser {

    private String id;

    private String title;

    private String content;

    private List<String> postIds;

    private int score;

    private boolean isPrivate;

    private int commentCount;

    private Date updatedAt;

    private User user;
}
