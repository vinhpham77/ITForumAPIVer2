package com.caykhe.itforum.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import java.util.Date;

@Data
@Builder
public class Image {
    @Id
    private String id;
    private String extension;
    private boolean status;
    private Date createdAt;
}
