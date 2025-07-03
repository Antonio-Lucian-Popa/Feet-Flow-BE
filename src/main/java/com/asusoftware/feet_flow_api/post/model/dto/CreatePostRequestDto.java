package com.asusoftware.feet_flow_api.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDto {
    private String title;
    private String description;
    private boolean isPublic;
    private List<MultipartFile> media;
}

