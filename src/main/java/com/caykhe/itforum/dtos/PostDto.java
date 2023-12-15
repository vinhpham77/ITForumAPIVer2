package com.caykhe.itforum.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;


@Data
public class PostDto {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @Size(min = 1, max = 3, message = "Phải có tối thiểu 1 tag và tối đa 3 tag")
    @NotNull(message = "Tag không được để trống")
    private List<String> tags;

    @JsonProperty("isPrivate")
    private boolean isPrivate;
}
