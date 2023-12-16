package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkInfo {
//    @Field(name = "itemId", targetType = FieldType.OBJECT_ID)
    private String itemId;
    private String type;
}
