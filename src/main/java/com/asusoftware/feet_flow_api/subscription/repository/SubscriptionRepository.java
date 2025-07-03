package com.asusoftware.feet_flow_api.subscription.repository;

import com.asusoftware.feet_flow_api.subscription.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findBySubscriberIdAndCreatorId(UUID subscriberId, UUID creatorId);

    Page<Subscription> findAllBySubscriberId(UUID subscriberId, Pageable pageable);

    Page<Subscription> findAllByCreatorId(UUID creatorId, Pageable pageable);

    boolean existsBySubscriberIdAndCreatorId(UUID subscriberId, UUID creatorId);
}
