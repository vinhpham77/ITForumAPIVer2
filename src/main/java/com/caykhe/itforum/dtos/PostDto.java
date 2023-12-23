package com.caykhe.itforum.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDto {
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 100, message = "Tiêu đề không được quá 100 ký tự")
    String title;

    @NotBlank(message = "Nội dung không được để trống")
    String content;

    @Size(min = 1, max = 3, message = "Phải có tối thiểu 1 tag và tối đa 3 tag")
    @NotNull(message = "Tag không được để trống")
    List<String> tags;

    @NotNull(message = "Mức độ chia sẻ phải được chỉ định")
    Boolean isPrivate;
}