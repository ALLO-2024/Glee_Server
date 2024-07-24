package com.allo.server.domain.post_like.service;

import static com.allo.server.error.ErrorCode.POST_NOT_FOUND;
import static com.allo.server.error.ErrorCode.USER_NOT_FOUND;

import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.post.repository.PostRepository;
import com.allo.server.domain.post_like.dto.response.PostLikeResponse;
import com.allo.server.domain.post_like.entity.PostLike;
import com.allo.server.domain.post_like.repository.PostLikeRepository;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostLikeRepository postLikeRepository;

  @Transactional
  public PostLikeResponse postLikeToggle(String email, Long postId) {
    UserEntity userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new BadRequestException(POST_NOT_FOUND));

    Optional<PostLike> postLike = postLikeRepository.findByUserEntityAndPost(userEntity, post);
    if (postLike.isPresent()) {
      postLikeRepository.delete(postLike.get());

      return PostLikeResponse.builder()
          .postId(post.getPostId())
          .isFavorite(false)
          .build();
    }
    else {
      PostLike newPostLike = PostLike.builder()
          .post(post)
          .userEntity(userEntity)
          .build();
      postLikeRepository.save(newPostLike);

      return PostLikeResponse.builder()
          .postId(post.getPostId())
          .isFavorite(true)
          .build();
    }
  }
}
