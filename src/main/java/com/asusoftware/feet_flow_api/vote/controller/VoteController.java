package com.asusoftware.feet_flow_api.vote.controller;

import com.asusoftware.feet_flow_api.common.ApiResponse;
import com.asusoftware.feet_flow_api.vote.model.dto.VoteCountDto;
import com.asusoftware.feet_flow_api.vote.model.dto.VoteRequestDto;
import com.asusoftware.feet_flow_api.vote.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> vote(@RequestBody @Valid VoteRequestDto request,
                                                  @AuthenticationPrincipal Jwt jwt) {
        voteService.vote(jwt, request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Void>> removeVote(@PathVariable UUID postId,
                                                        @AuthenticationPrincipal Jwt jwt) {
        voteService.removeVote(jwt, postId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<ApiResponse<VoteCountDto>> getVoteCount(@PathVariable UUID postId) {
        return ResponseEntity.ok(ApiResponse.ok(voteService.getVoteCount(postId)));
    }
}
