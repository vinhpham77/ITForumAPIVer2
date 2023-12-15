package com.caykhe.itforum.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
   @NotBlank(message = "Email không được để trống")
   private String email;
}
