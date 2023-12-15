package com.caykhe.itforum.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class Series {
    @Id
    @Pattern(regexp = "^[a-f\\d]{24}$", message = "ID không hợp lệ")
    private String id;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;
    
    @NotNull(message = "Danh sách bài viết không được để trống")
//    @Field(name="postIds",targetType = FieldType.OBJECT_ID)
    private List<String> postIds;

    @NotNull(message = "Điểm không được để trống")
    private int score;

    @Min(value = 0, message = "Số lượt bình luận không thể nhỏ hơn 0")
    private int commentCount;

    @JsonProperty("isPrivate")
    private boolean isPrivate;

    @NotBlank(message = "Tác giả không được để trống")
    private String createdBy;

    @PastOrPresent(message = "Ngày cập nhật không hợp lệ")
    @NotNull(message = "Ngày cập nhật không được để trống")
    private Date updatedAt;
}
