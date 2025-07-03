package com.asusoftware.feet_flow_api.post.model.dto;

import com.asusoftware.feet_flow_api.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorSummaryDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private UserRole role;
}
