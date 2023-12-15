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
public class Post {
    @Id
    @Pattern(regexp = "^[a-f\\d]{24}$", message = "ID không hợp lệ")
    private String id;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @Size(min = 1, max = 3, message = "Phải có tối thiểu 1 tag và tối đa 3 tag")
    @NotNull(message = "Tag không được để trống")
    private List<String> tags;

    @NotNull(message = "Điểm không được để trống")
    private int score;

//    @JsonProperty("isPrivate")
    private boolean isPrivate;
    
    @Min(value = 0, message = "Số lượt bình luận không thể nhỏ hơn 0")
    private int commentCount;

    @NotBlank(message = "Tác giả không được để trống")
    private String createdBy;

    @PastOrPresent(message = "Ngày cập nhật không hợp lệ")
    @NotNull(message = "Ngày cập nhật không được để trống")
    private Date updatedAt;
}
