package com.asusoftware.feet_flow_api.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String coverImageUrl;
    private String bio;
    private UserStatsDto stats;
}
