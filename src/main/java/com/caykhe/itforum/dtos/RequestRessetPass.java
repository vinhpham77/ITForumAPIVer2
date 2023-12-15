package com.caykhe.itforum.dtos;

import lombok.Data;

@Data
public class RequestRessetPass {
private String username;
private String newPassword;
private String otp;

}
