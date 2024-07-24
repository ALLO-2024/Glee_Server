package com.allo.server.domain.post_like.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostLikeResponse {

  @NotNull
  private Long postId;

  @NotNull
  private Boolean isFavorite;
}
