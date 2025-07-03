package com.asusoftware.feet_flow_api.comment.service;

import com.asusoftware.feet_flow_api.comment.model.Comment;
import com.asusoftware.feet_flow_api.comment.model.dto.CommentAuthorDto;
import com.asusoftware.feet_flow_api.comment.model.dto.CommentRequestDto;
import com.asusoftware.feet_flow_api.comment.model.dto.CommentResponseDto;
import com.asusoftware.feet_flow_api.comment.repository.CommentRepository;
import com.asusoftware.feet_flow_api.user.model.User;
import com.asusoftware.feet_flow_api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final Clock clock = Clock.systemUTC();

    public Page<CommentResponseDto> getByPost(UUID postId, int page, int size) {
        return commentRepository.findAllByPostId(postId, PageRequest.of(page, size))
                .map(this::mapToDto);
    }

    @Transactional
    public CommentResponseDto create(Jwt jwt, CommentRequestDto request) {
        UUID userId = UUID.fromString(jwt.getSubject());

        Comment comment = Comment.builder()
                .userId(userId)
                .postId(request.getPostId())
                .content(request.getContent())
                .createdAt(Instant.now(clock))
                .build();
        commentRepository.save(comment);

        return mapToDto(comment);
    }

    @Transactional
    public CommentResponseDto update(Jwt jwt, UUID id, CommentRequestDto request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to edit this comment");
        }

        comment.setContent(request.getContent());
        commentRepository.save(comment);
        return mapToDto(comment);
    }

    @Transactional
    public void delete(Jwt jwt, UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentResponseDto mapToDto(Comment comment) {
        User user = userRepository.findById(comment.getUserId())
                .orElse(null);

        CommentAuthorDto author = user != null ? CommentAuthorDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build() : null;

        return CommentResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .author(author)
                .build();
    }
}

