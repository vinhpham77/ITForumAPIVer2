package com.caykhe.itforum.dtos;

import com.caykhe.itforum.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class SeriesDto {
    Integer id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 100, message = "Tiêu đề không được quá 100 ký tự")
    String title;

    @NotBlank(message = "Nội dung không được để trống")
    String content;

    List<Integer> postIds;

    @NotNull(message = "Mức độ chia sẻ phải được chỉ định")
    Boolean isPrivate;

    Integer score;

    Integer commentCount;

    Instant updatedAt;

    User createdBy;
}
