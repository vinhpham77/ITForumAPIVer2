package com.caykhe.itforum.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Builder
public class Vote {
    @Id
    private String id;
    private String postId;
    private Boolean type;
    private String username;
    private Date updatedAt;
}
