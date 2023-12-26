package com.caykhe.itforum.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileStats {
    private int postCount;
    private int questionCount;
    private int seriesCount;
    private int followingCount;
    private int followerCount;
}
