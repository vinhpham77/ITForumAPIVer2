package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkPostRequest {
    private Integer targetId;
    private Boolean type;
}
