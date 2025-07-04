package com.asusoftware.feet_flow_api.user.model.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatsDto {
    private int followersCount;
    private int followingCount;
    private int likesCount;
    private int postsCount;
    private int subscribersCount;
}
