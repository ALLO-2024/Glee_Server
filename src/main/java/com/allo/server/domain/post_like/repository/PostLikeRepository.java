package com.allo.server.domain.post_like.repository;

import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.post_like.entity.PostLike;
import com.allo.server.domain.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  Optional<PostLike> findByUserEntityAndPost(UserEntity userEntity, Post post);
}
