package com.asusoftware.feet_flow_api.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private UUID id;
    private UUID creatorId;
    private String title;
    private String description;
    private boolean isPublic;
    private Instant createdAt;
    private List<PostMediaDto> media;

    private CreatorSummaryDto creator;
}
