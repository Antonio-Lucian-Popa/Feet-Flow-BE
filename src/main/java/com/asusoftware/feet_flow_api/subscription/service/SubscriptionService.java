package com.asusoftware.feet_flow_api.subscription.service;

import com.asusoftware.feet_flow_api.subscription.model.Subscription;
import com.asusoftware.feet_flow_api.subscription.model.dto.SubscriptionCheckDto;
import com.asusoftware.feet_flow_api.subscription.model.dto.SubscriptionDto;
import com.asusoftware.feet_flow_api.subscription.repository.SubscriptionRepository;
import com.asusoftware.feet_flow_api.user.model.User;
import com.asusoftware.feet_flow_api.user.model.UserRole;
import com.asusoftware.feet_flow_api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Transactional
    public void subscribe(Jwt jwt, UUID creatorId) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User subscriber = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        if (!creator.getRole().equals(UserRole.CREATOR)) {
            throw new RuntimeException("Target user is not a creator");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1);

        Subscription subscription = subscriptionRepository.findBySubscriberIdAndCreatorId(subscriber.getId(), creatorId)
                .map(existing -> {
                    existing.setStartDate(startDate);
                    existing.setEndDate(endDate);
                    existing.setActive(true);
                    return existing;
                })
                .orElse(Subscription.builder()
                        .subscriberId(subscriber.getId())
                        .creatorId(creatorId)
                        .startDate(startDate)
                        .endDate(endDate)
                        .isActive(true)
                        .build());

        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void unsubscribe(Jwt jwt, UUID creatorId) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User subscriber = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        subscriptionRepository.findBySubscriberIdAndCreatorId(subscriber.getId(), creatorId)
                .ifPresent(subscriptionRepository::delete);
    }

    public Page<SubscriptionDto> getMySubscriptions(Jwt jwt, int page, int size) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User subscriber = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return subscriptionRepository.findAllBySubscriberId(subscriber.getId(), PageRequest.of(page, size))
                .map(sub -> mapper.map(sub, SubscriptionDto.class));
    }

    public Page<SubscriptionDto> getMySubscribers(Jwt jwt, int page, int size) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User creator = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return subscriptionRepository.findAllByCreatorId(creator.getId(), PageRequest.of(page, size))
                .map(sub -> mapper.map(sub, SubscriptionDto.class));
    }

    public SubscriptionCheckDto checkSubscription(Jwt jwt, UUID creatorId) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        User subscriber = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return subscriptionRepository.findBySubscriberIdAndCreatorId(subscriber.getId(), creatorId)
                .map(sub -> SubscriptionCheckDto.builder()
                        .id(sub.getId())
                        .isActive(sub.isActive())
                        .endDate(sub.getEndDate())
                        .build())
                .orElse(SubscriptionCheckDto.builder()
                        .id(null)
                        .isActive(false)
                        .endDate(null)
                        .build());
    }
}

