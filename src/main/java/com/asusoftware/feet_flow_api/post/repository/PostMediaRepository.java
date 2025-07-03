package com.asusoftware.feet_flow_api.post.repository;

import com.asusoftware.feet_flow_api.post.model.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
    List<PostMedia> findByPostIdOrderByOrderIndex(UUID postId);
}

