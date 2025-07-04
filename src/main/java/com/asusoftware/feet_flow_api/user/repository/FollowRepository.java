package com.asusoftware.feet_flow_api.user.repository;

import com.asusoftware.feet_flow_api.user.model.Follow;
import com.asusoftware.feet_flow_api.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {

    boolean existsByFollowerIdAndTargetId(UUID followerId, UUID targetId);

    void deleteByFollowerIdAndTargetId(UUID followerId, UUID targetId);

    @Query("""
           SELECT u FROM User u
           WHERE u.id IN (
               SELECT f.followerId FROM Follow f
               WHERE f.targetId = :userId
           )
           """)
    Page<User> findFollowers(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
           SELECT u FROM User u
           WHERE u.id IN (
               SELECT f.targetId FROM Follow f
               WHERE f.followerId = :userId
           )
           """)
    Page<User> findFollowing(@Param("userId") UUID userId, Pageable pageable);

    Page<Follow> findAllByTargetId(UUID targetId, Pageable pageable);
    Page<Follow> findAllByFollowerId(UUID followerId, Pageable pageable);

    @Query(value = "SELECT count_followers(:userId)", nativeQuery = true)
    int countByTargetId(@Param("userId") UUID userId);

    @Query(value = "SELECT count_following(:userId)", nativeQuery = true)
    int countByFollowerId(@Param("userId") UUID userId);
}
