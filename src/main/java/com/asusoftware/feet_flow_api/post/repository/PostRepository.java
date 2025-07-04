package com.asusoftware.feet_flow_api.post.repository;

import com.asusoftware.feet_flow_api.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findAllByIsPublicTrue(Pageable pageable);

    Page<Post> findAllByCreatorId(UUID creatorId, Pageable pageable);

    Optional<Post> findByIdAndIsPublicTrue(UUID id);

    List<Post> findAllByIdIn(List<Long> ids);

    @Query(value = "SELECT count_posts_by_creator(:creatorId)", nativeQuery = true)
    int countPostsByUserId(@Param("creatorId") UUID creatorId);
}

