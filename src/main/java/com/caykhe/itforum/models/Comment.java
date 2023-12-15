package com.caykhe.itforum.models;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class Comment {
    @Id
    private String id;

//    @Field(name = "contentId", targetType = FieldType.OBJECT_ID)
    private String contentId;
    private Type type;
    private List<SubComment> comments;
}