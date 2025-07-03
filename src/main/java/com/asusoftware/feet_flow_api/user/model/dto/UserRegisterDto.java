package com.asusoftware.feet_flow_api.user.model.dto;

import com.asusoftware.feet_flow_api.user.model.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
}

