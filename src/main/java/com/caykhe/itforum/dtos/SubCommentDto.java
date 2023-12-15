package com.caykhe.itforum.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubCommentDto {
    private String subCommentFatherId;

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;
}
