package com.asusoftware.feet_flow_api.comment.controller;

import com.asusoftware.feet_flow_api.comment.model.dto.CommentRequestDto;
import com.asusoftware.feet_flow_api.comment.model.dto.CommentResponseDto;
import com.asusoftware.feet_flow_api.comment.service.CommentService;
import com.asusoftware.feet_flow_api.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Page<CommentResponseDto>>> getCommentsForPost(@PathVariable UUID postId,
                                                                                    @RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.getByPost(postId, page, size)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> create(@AuthenticationPrincipal Jwt jwt,
                                                 @RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.create(jwt, request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> update(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable UUID id,
                                                 @RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.update(jwt, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable UUID id) {
        commentService.delete(jwt, id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
