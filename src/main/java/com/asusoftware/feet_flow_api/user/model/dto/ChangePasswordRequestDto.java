package com.asusoftware.feet_flow_api.user.model.dto;

import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    private String currentPassword;
    private String newPassword;
}

