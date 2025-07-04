package com.asusoftware.feet_flow_api.vote.repository;

import com.asusoftware.feet_flow_api.vote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    Optional<Vote> findByUserIdAndPostId(UUID userId, UUID postId);

    int countByPostIdAndValue(UUID postId, int value); // upvotes or downvotes

    void deleteByUserIdAndPostId(UUID userId, UUID postId);

    @Query(value = "SELECT count_likes_for_creator(:creatorId)", nativeQuery = true)
    int countLikesByUserId(@Param("creatorId") UUID creatorId);
}

