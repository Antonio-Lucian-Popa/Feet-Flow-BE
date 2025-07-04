package com.asusoftware.feet_flow_api.post.service;

import com.asusoftware.feet_flow_api.auth.service.AuthService;
import com.asusoftware.feet_flow_api.post.model.Post;
import com.asusoftware.feet_flow_api.post.model.PostMedia;
import com.asusoftware.feet_flow_api.post.model.dto.*;
import com.asusoftware.feet_flow_api.post.repository.PostMediaRepository;
import com.asusoftware.feet_flow_api.post.repository.PostRepository;
import com.asusoftware.feet_flow_api.user.model.User;
import com.asusoftware.feet_flow_api.user.repository.UserRepository;
import com.asusoftware.feet_flow_api.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final UserRepository userRepository;
    private final MediaStorageService mediaStorageService;
    private final Clock clock = Clock.systemUTC();
    private final AuthService authService;

    public Page<PostResponseDto> getAllPublicPosts(int page, int size) {
        return postRepository.findAllByIsPublicTrue(PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    public Page<PostResponseDto> getPostsByCreator(UUID creatorId, int page, int size) {
        return postRepository.findAllByCreatorId(creatorId, PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    public PostResponseDto getPostById(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return mapToResponse(post);
    }

    public Page<PostResponseDto> getFeed(Jwt jwt, int page, int size) {
        UUID userId = UUID.fromString(jwt.getSubject());
        // Simplificare: returnăm toate postările publice (faza MVP)
        return getAllPublicPosts(page, size);
    }

    @Transactional
    public PostResponseDto create(Jwt jwt, CreatePostRequestDto request) {
        User user = authService.getCurrentUserEntity(jwt);

        Post post = Post.builder()
                .creatorId(user.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .isPublic(request.isPublic())
                .createdAt(Instant.now(clock))
                .build();
        postRepository.save(post);

        List<PostMedia> mediaList = new ArrayList<>();
        int order = 0;
        for (MultipartFile file : request.getMedia()) {
            String url = mediaStorageService.upload(file, post.getId());
            String mediaType = file.getContentType() != null && file.getContentType().startsWith("video") ? "video" : "photo";

            mediaList.add(PostMedia.builder()
                    .postId(post.getId())
                    .mediaUrl(url)
                    .mediaType(mediaType)
                    .orderIndex(order++)
                    .build());
        }
        postMediaRepository.saveAll(mediaList);

        return mapToResponse(post);
    }

    @Transactional
    public PostResponseDto update(Jwt jwt, UUID postId, UpdatePostRequestDto request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getCreatorId().equals(userId)) {
            throw new RuntimeException("Unauthorized to edit this post");
        }

        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setPublic(request.isPublic());
        postRepository.save(post);

        return mapToResponse(post);
    }

    @Transactional
    public void delete(Jwt jwt, UUID postId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getCreatorId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this post");
        }

        postMediaRepository.deleteAll(postMediaRepository.findByPostIdOrderByOrderIndex(postId));
        postRepository.delete(post);
    }

    private PostResponseDto mapToResponse(Post post) {
        List<PostMedia> media = postMediaRepository.findByPostIdOrderByOrderIndex(post.getId());
        Optional<User> creatorOpt = userRepository.findById(post.getCreatorId());

        CreatorSummaryDto creator = creatorOpt.map(user -> CreatorSummaryDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .role(user.getRole())
                .build()).orElse(null);

        List<PostMediaDto> mediaDtos = media.stream().map(m -> PostMediaDto.builder()
                .id(m.getId())
                .mediaUrl(m.getMediaUrl())
                .mediaType(m.getMediaType())
                .thumbnailUrl(m.getThumbnailUrl())
                .orderIndex(m.getOrderIndex())
                .build()).collect(Collectors.toList());

        return PostResponseDto.builder()
                .id(post.getId())
                .creatorId(post.getCreatorId())
                .title(post.getTitle())
                .description(post.getDescription())
                .isPublic(post.isPublic())
                .createdAt(post.getCreatedAt())
                .creator(creator)
                .media(mediaDtos)
                .build();
    }
}
