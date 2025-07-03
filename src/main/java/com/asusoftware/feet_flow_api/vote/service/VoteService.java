package com.asusoftware.feet_flow_api.vote.service;

import com.asusoftware.feet_flow_api.post.model.Post;
import com.asusoftware.feet_flow_api.post.repository.PostRepository;
import com.asusoftware.feet_flow_api.user.model.User;
import com.asusoftware.feet_flow_api.user.repository.UserRepository;
import com.asusoftware.feet_flow_api.vote.model.Vote;
import com.asusoftware.feet_flow_api.vote.model.dto.VoteCountDto;
import com.asusoftware.feet_flow_api.vote.model.dto.VoteRequestDto;
import com.asusoftware.feet_flow_api.vote.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void vote(Jwt jwt, VoteRequestDto request) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        voteRepository.findByUserIdAndPostId(user.getId(), post.getId()).ifPresentOrElse(
                existing -> {
                    existing.setValue(request.getValue());
                    voteRepository.save(existing);
                },
                () -> {
                    Vote vote = Vote.builder()
                            .userId(user.getId())
                            .postId(post.getId())
                            .value(request.getValue())
                            .build();
                    voteRepository.save(vote);
                }
        );
    }

    @Transactional
    public void removeVote(Jwt jwt, UUID postId) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        voteRepository.deleteByUserIdAndPostId(user.getId(), postId);
    }

    public VoteCountDto getVoteCount(UUID postId) {
        int upvotes = voteRepository.countByPostIdAndValue(postId, 1);
        int downvotes = voteRepository.countByPostIdAndValue(postId, -1);
        return VoteCountDto.builder()
                .upVotes(upvotes)
                .downVotes(downvotes)
                .build();
    }
}
