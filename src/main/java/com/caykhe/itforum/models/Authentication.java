package com.caykhe.itforum.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Authentication {
    @Id
    private String id;

//    @Indexed(unique = true)
    private String username;

    @NotNull
    private String refreshToken;
}
