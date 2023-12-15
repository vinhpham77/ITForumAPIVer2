package com.caykhe.itforum.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Tài khoản không được để trống")
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String currentPassword;
    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;
}
