package com.caykhe.itforum.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateRequest {
    private String username;
    private String displayName;
    private boolean gender;
    private Date birthdate;
}
