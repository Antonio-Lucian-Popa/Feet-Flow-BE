package com.asusoftware.feet_flow_api.post.controller;

import com.asusoftware.feet_flow_api.common.ApiResponse;
import com.asusoftware.feet_flow_api.post.model.dto.CreatePostRequestDto;
import com.asusoftware.feet_flow_api.post.model.dto.UpdatePostRequestDto;
import com.asusoftware.feet_flow_api.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getAllPublicPosts(page, size)));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<?>> getFeed(@AuthenticationPrincipal Jwt jwt,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getFeed(jwt, page, size)));
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<ApiResponse<?>> getCreatorPosts(@PathVariable UUID creatorId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPostsByCreator(creatorId, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPostById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@AuthenticationPrincipal Jwt jwt,
                                                 @RequestPart("isPublic") boolean isPublic,
                                                 @RequestPart(value = "title", required = false) String title,
                                                 @RequestPart(value = "description", required = false) String description,
                                                 @RequestPart("media") List<MultipartFile> media) {
        CreatePostRequestDto request = CreatePostRequestDto.builder()
                .isPublic(isPublic)
                .title(title)
                .description(description)
                .media(media)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(postService.create(jwt, request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable UUID id,
                                                 @RequestBody @Valid UpdatePostRequestDto request) {
        return ResponseEntity.ok(ApiResponse.ok(postService.update(jwt, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable UUID id) {
        postService.delete(jwt, id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}

