package com.asusoftware.feet_flow_api.user.service;

import com.asusoftware.feet_flow_api.user.model.User;
import com.asusoftware.feet_flow_api.user.model.UserRole;
import com.asusoftware.feet_flow_api.user.model.dto.UpdateProfileRequestDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserSummaryDto;
import com.asusoftware.feet_flow_api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDto getById(UUID id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDto getMe(Jwt jwt) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        return userRepository.findByKeycloakId(keycloakId)
                .map(user -> modelMapper.map(user, UserDto.class))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public UserDto updateProfile(Jwt jwt, UpdateProfileRequestDto request) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBio(request.getBio());

        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    public void updateProfilePicture(Jwt jwt, String url) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfilePictureUrl(url);
        userRepository.save(user);
    }

    public Page<UserSummaryDto> getCreators(int page, int size) {
        return userRepository.findAllByRole(UserRole.CREATOR.name(), PageRequest.of(page, size))
                .map(user -> modelMapper.map(user, UserSummaryDto.class));
    }

    public Page<UserSummaryDto> search(String query, int page, int size) {
        return userRepository.searchByName(query, PageRequest.of(page, size))
                .map(user -> modelMapper.map(user, UserSummaryDto.class));
    }
}
