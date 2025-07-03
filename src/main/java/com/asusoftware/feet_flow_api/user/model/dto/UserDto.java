package com.asusoftware.feet_flow_api.user.model.dto;

import com.asusoftware.feet_flow_api.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String profilePictureUrl;
    private Instant createdAt;
    private UserRole role;
}