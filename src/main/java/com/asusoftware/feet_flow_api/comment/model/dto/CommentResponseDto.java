package com.asusoftware.feet_flow_api.comment.model.dto;

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
public class CommentResponseDto {
    private UUID id;
    private UUID userId;
    private UUID postId;
    private String content;
    private Instant createdAt;
    private CommentAuthorDto author;
}
