package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class VoteRequest {
    private Integer targetId;
    private Boolean type;
    private String username;
}
