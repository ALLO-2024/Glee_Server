package com.allo.server.domain.post_like.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostLikeRequest {

  @NotNull
  private Long postId;
}
