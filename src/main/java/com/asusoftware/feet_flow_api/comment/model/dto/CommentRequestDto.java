package com.asusoftware.feet_flow_api.comment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private UUID postId;
    private String content;
}
