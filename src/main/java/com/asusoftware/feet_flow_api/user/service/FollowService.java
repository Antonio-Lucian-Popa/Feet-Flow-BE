package com.asusoftware.feet_flow_api.user.service;

import com.asusoftware.feet_flow_api.auth.service.AuthService;
import com.asusoftware.feet_flow_api.post.repository.PostRepository;
import com.asusoftware.feet_flow_api.subscription.repository.SubscriptionRepository;
import com.asusoftware.feet_flow_api.user.model.Follow;
import com.asusoftware.feet_flow_api.user.model.User;
import com.asusoftware.feet_flow_api.user.model.dto.UserDto;
import com.asusoftware.feet_flow_api.user.model.dto.UserStatsDto;
import com.asusoftware.feet_flow_api.user.repository.FollowRepository;
import com.asusoftware.feet_flow_api.user.repository.UserRepository;
import com.asusoftware.feet_flow_api.vote.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;
    private final ModelMapper mapper;

    public UserStatsDto getStats(UUID userId) {
        int followers = followRepository.countByTargetId(userId);
        int following = followRepository.countByFollowerId(userId);
        int posts = postRepository.countPostsByUserId(userId);
        int likes = voteRepository.countLikesByUserId(userId);
        int subs = subscriptionRepository.countSubscribersByUserId(userId);
        return UserStatsDto.builder()
                .followersCount(followers)
                .followingCount(following)
                .postsCount(posts)
                .likesCount(likes)
                .subscribersCount(subs)
                .build();
    }

    @Transactional
    public void follow(Jwt userId, UUID targetId) {
        User user = authService.getCurrentUserEntity(userId);
        if (user.getId().equals(targetId)) throw new RuntimeException("Cannot follow yourself");
        if (followRepository.existsByFollowerIdAndTargetId(user.getId(), targetId)) return;
        followRepository.save(Follow.builder().followerId(user.getId()).targetId(targetId).build());
    }

    @Transactional
    public void unfollow(Jwt userId, UUID targetId) {
        User user = authService.getCurrentUserEntity(userId);
        followRepository.deleteByFollowerIdAndTargetId(user.getId(), targetId);
    }

    public boolean isFollowing(Jwt userId, UUID targetId) {
        User user = authService.getCurrentUserEntity(userId);
        return followRepository.existsByFollowerIdAndTargetId(user.getId(), targetId);
    }

    public Page<UserDto> getFollowers(UUID userId, Pageable pageable) {
        return followRepository.findFollowers(userId, pageable)
                .map(user -> mapper.map(user, UserDto.class));
    }

    public Page<UserDto> getFollowing(UUID userId, Pageable pageable) {
        return followRepository.findFollowing(userId, pageable)
                .map(user -> mapper.map(user, UserDto.class));
    }
}
