package com.asusoftware.feet_flow_api.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDto {
    private String title;
    private String description;
    private boolean isPublic;
}
