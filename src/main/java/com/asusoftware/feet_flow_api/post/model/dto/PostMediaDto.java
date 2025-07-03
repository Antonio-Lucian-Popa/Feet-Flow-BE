package com.asusoftware.feet_flow_api.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMediaDto {
    private Long id;
    private String mediaUrl;
    private String mediaType;
    private String thumbnailUrl;
    private Integer orderIndex;
}
