package com.asusoftware.feet_flow_api.subscription.controller;

import com.asusoftware.feet_flow_api.common.ApiResponse;
import com.asusoftware.feet_flow_api.subscription.model.dto.SubscriptionCheckDto;
import com.asusoftware.feet_flow_api.subscription.model.dto.SubscriptionDto;
import com.asusoftware.feet_flow_api.subscription.model.dto.SubscriptionRequestDto;
import com.asusoftware.feet_flow_api.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> subscribe(@RequestBody @Valid SubscriptionRequestDto request,
                                                       @AuthenticationPrincipal Jwt jwt) {
        subscriptionService.subscribe(jwt, request.getCreatorId());
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/creator/{creatorId}")
    public ResponseEntity<ApiResponse<Void>> unsubscribe(@PathVariable UUID creatorId,
                                                         @AuthenticationPrincipal Jwt jwt) {
        subscriptionService.unsubscribe(jwt, creatorId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<SubscriptionDto>>> getMySubscriptions(@AuthenticationPrincipal Jwt jwt,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(subscriptionService.getMySubscriptions(jwt, page, size)));
    }

    @GetMapping("/creator")
    public ResponseEntity<ApiResponse<Page<SubscriptionDto>>> getMySubscribers(@AuthenticationPrincipal Jwt jwt,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(subscriptionService.getMySubscribers(jwt, page, size)));
    }

    @GetMapping("/check/{creatorId}")
    public ResponseEntity<ApiResponse<SubscriptionCheckDto>> checkSubscription(@AuthenticationPrincipal Jwt jwt,
                                                                               @PathVariable UUID creatorId) {
        return ResponseEntity.ok(ApiResponse.ok(subscriptionService.checkSubscription(jwt, creatorId)));
    }
}

