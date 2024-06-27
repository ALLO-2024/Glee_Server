package com.allo.server.domain.post_like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.post.repository.PostRepository;
import com.allo.server.domain.post_like.dto.response.PostLikeResponse;
import com.allo.server.domain.post_like.repository.PostLikeRepository;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostLikeServiceTest {

  @InjectMocks
  PostLikeService postLikeService;

  @Mock
  UserRepository userRepository;

  @Mock
  PostLikeRepository postLikeRepository;

  @Mock
  PostRepository postRepository;

  @Test
  @DisplayName("좋아요 토글")
  void likeToggleTest() {
    // given
    UserEntity userEntity = UserEntity.builder()
        .email("test@naver.com")
        .build();
    Post post = Post.builder()
        .userEntity(userEntity)
        .title("title1")
        .content("content")
        .build();
    doReturn(Optional.of(userEntity)).when(userRepository)
        .findByEmail("test@naver.com");
    doReturn(Optional.of(post)).when(postRepository)
        .findById(post.getPostId());
    doReturn(Optional.empty()).when(postLikeRepository)
        .findByUserEntityAndPost(userEntity, post);

    // when
    PostLikeResponse result = postLikeService.postLikeToggle("test@naver.com",
        post.getPostId());

    // then
    assertThat(result.getIsFavorite()).isEqualTo(true);
  }
}
