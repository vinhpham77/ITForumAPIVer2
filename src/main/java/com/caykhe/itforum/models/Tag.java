package com.caykhe.itforum.models;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Tag {
    @Id
    @Pattern(regexp = "^[a-zA-Z0-9]{24}$", message = "ID không hợp lệ")
    private String id;
    
//    @Indexed(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "Tên tag không hợp lệ")
    private String name;
    
    private String description;
}
