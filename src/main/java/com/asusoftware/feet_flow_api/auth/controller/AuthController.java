package com.asusoftware.feet_flow_api.auth.controller;

import com.asusoftware.feet_flow_api.auth.service.AuthService;
import com.asusoftware.feet_flow_api.common.ApiResponse;
import com.asusoftware.feet_flow_api.user.model.dto.LoginDto;
import com.asusoftware.feet_flow_api.user.model.dto.LoginResponseDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserRegisterDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@RequestBody UserRegisterDto request) {
        UserResponseDto response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> me(@AuthenticationPrincipal Jwt jwt) {
        UserResponseDto user = authService.getCurrentUser(jwt);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDto>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        return ResponseEntity.ok(ApiResponse.ok(authService.refresh(refreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // optional logic
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}

