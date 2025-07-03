package com.asusoftware.feet_flow_api.user.controller;

import com.asusoftware.feet_flow_api.common.ApiResponse;
import com.asusoftware.feet_flow_api.post.service.MediaStorageService;
import com.asusoftware.feet_flow_api.user.model.dto.UpdateProfileRequestDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserSummaryDto;
import com.asusoftware.feet_flow_api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final MediaStorageService mediaStorageService;

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
        String url = mediaStorageService.upload(file);
        userService.updateProfilePicture(jwt, url);
        return ResponseEntity.ok(ApiResponse.ok(url));
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
}

