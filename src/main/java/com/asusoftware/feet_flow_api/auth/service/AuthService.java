package com.asusoftware.feet_flow_api.auth.service;

import com.asusoftware.feet_flow_api.config.KeycloakService;
import com.asusoftware.feet_flow_api.user.model.User;
import com.asusoftware.feet_flow_api.user.model.dto.LoginDto;
import com.asusoftware.feet_flow_api.user.model.dto.LoginResponseDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserRegisterDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserResponseDto;
import com.asusoftware.feet_flow_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakService keycloakService;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final Clock clock = Clock.systemUTC();

    /**
     * Înregistrează un utilizator în Keycloak și în baza locală.
     */
    @Transactional
    public UserResponseDto register(UserRegisterDto dto) {
        if (dto.getRole() == null) {
            throw new IllegalArgumentException("Rolul utilizatorului este obligatoriu.");
        }

        // 1. Creează userul în Keycloak
        String keycloakId = keycloakService.createKeycloakUser(dto);

        // 2. Creează local userul
        User user = User.builder()
                .keycloakId(UUID.fromString(keycloakId))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .role(dto.getRole().name().toLowerCase()) // "user", "creator"
                .createdAt(Instant.now(clock))
                .build();

        userRepository.save(user);
        return mapper.map(user, UserResponseDto.class);
    }

    /**
     * Login și obținere token de la Keycloak.
     */
    public LoginResponseDto login(LoginDto dto) {
        return keycloakService.loginUser(dto);
    }

    /**
     * Refresh token din Keycloak.
     */
    public LoginResponseDto refresh(String refreshToken) {
        return keycloakService.refreshToken(refreshToken);
    }

    /**
     * Returnează info despre userul logat.
     */
    public UserResponseDto getCurrentUser(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    /**
     * Returnează entitatea completă pentru userul curent.
     */
    public User getCurrentUserEntity(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Returnează doar UUID-ul userului logat.
     */
    public UUID getUserIdFromKeycloak(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }
}
