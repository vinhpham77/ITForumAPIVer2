package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookmarkRequest {
    private List<String> postIds;
    private String username;

}
