package com.allo.server.domain.post_image.entity;

import com.allo.server.domain.post.entity.Post;
import com.allo.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class PostImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String postImageUrl;

    @Builder
    public PostImage(Post post, String postImageUrl) {
        this.post = post;
        this.postImageUrl = postImageUrl;
    }
}
