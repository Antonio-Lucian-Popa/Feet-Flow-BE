package com.asusoftware.feet_flow_api.user.controller;

import com.asusoftware.feet_flow_api.common.ApiResponse;
import com.asusoftware.feet_flow_api.config.KeycloakService;
import com.asusoftware.feet_flow_api.post.service.MediaStorageService;
import com.asusoftware.feet_flow_api.user.model.dto.*;
import com.asusoftware.feet_flow_api.user.service.FollowService;
import com.asusoftware.feet_flow_api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;
    private final KeycloakService keycloakService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getById(id)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getMe(jwt)));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(@AuthenticationPrincipal Jwt jwt,
                                                              @RequestBody @Valid UpdateProfileRequestDto request) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateProfile(jwt, request)));
    }

    @PostMapping("/profile/picture")
    public ResponseEntity<ApiResponse<String>> uploadProfilePicture(@AuthenticationPrincipal Jwt jwt,
                                                                    @RequestParam("file") MultipartFile file) {
        String url = userService.updateProfilePicture(jwt, file);
        return ResponseEntity.ok(ApiResponse.ok(url));
    }

    @PostMapping("/profile/cover")
    public ResponseEntity<ApiResponse<UserDto>> uploadCoverImage(@AuthenticationPrincipal Jwt jwt,
                                                                 @RequestParam("coverImage") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateCoverImage(jwt, file)));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@AuthenticationPrincipal Jwt jwt,
                                                            @RequestBody ChangePasswordRequestDto request) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        keycloakService.changePassword(keycloakId, request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.ok());
    }



    @GetMapping("/creators")
    public ResponseEntity<ApiResponse<Page<UserSummaryDto>>> getCreators(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getCreators(page, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserSummaryDto>>> searchUsers(@RequestParam("q") String query,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(userService.search(query, page, size)));
    }


    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<UserStatsDto>> getUserStats(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(followService.getStats(id)));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<ApiResponse<Void>> follow(@PathVariable UUID id,
                                                    @AuthenticationPrincipal Jwt jwt) {
        followService.follow(jwt, id);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/{id}/follow")
    public ResponseEntity<ApiResponse<Void>> unfollow(@PathVariable UUID id,
                                                      @AuthenticationPrincipal Jwt jwt) {
        followService.unfollow(jwt, id);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/{id}/follow/status")
    public ResponseEntity<ApiResponse<?>> isFollowing(@PathVariable UUID id,
                                                      @AuthenticationPrincipal Jwt jwt) {
        boolean status = followService.isFollowing(jwt, id);
        return ResponseEntity.ok(ApiResponse.ok(status));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getFollowers(@PathVariable UUID id,
                                                                   Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(followService.getFollowers(id, pageable)));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getFollowing(@PathVariable UUID id,
                                                                   Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(followService.getFollowing(id, pageable)));
    }
}

