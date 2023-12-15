package com.caykhe.itforum.dtos;

import com.caykhe.itforum.models.Type;
import lombok.Data;

import java.util.List;

@Data
public class CommentAggregate {
    private String id;
    private String contentId;
    private Type type;
    private List<SubCommentAggregate> comments;
}
