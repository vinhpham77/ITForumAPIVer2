package com.caykhe.itforum.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class Bookmarks {
    @Id
    private String id;
    private List<BookmarkInfo> bookmarkInfoList;
    private String username;
}

