package com.asusoftware.feet_flow_api.post.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId; // FK to User

    private String title;

    private String description;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "created_at")
    private Instant createdAt;
}

