package com.caykhe.itforum.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import java.util.Date;

@Data
@Builder
public class Follow {
    @Id
    private String id;
    private String follower;
    private String followed;
    private Date createdAt;
}
