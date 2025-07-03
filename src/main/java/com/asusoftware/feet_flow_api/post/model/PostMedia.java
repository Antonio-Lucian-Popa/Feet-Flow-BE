package com.asusoftware.feet_flow_api.post.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "post_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMedia {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "post_id", nullable = false)
    private UUID postId; // FK to Post

    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @Column(name = "media_type", nullable = false)
    private String mediaType; // "photo" | "video"

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "order_index")
    private Integer orderIndex;
}
