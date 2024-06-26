package com.allo.server.domain.post_like.controller;

import com.allo.server.domain.post_like.dto.request.PostLikeRequest;
import com.allo.server.domain.post_like.dto.response.PostLikeResponse;
import com.allo.server.domain.post_like.service.PostLikeService;
import com.allo.server.global.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post-like")
public class PostLikeController {

  private final PostLikeService postLikeService;

  @PostMapping("/toggle")
  public ResponseEntity<PostLikeResponse> postLikeToggle(
      @AuthenticationPrincipal UserDetails loginUser,
      @RequestBody PostLikeRequest postLikeRequest) {

    PostLikeResponse postLikeResponse = postLikeService.postLikeToggle(loginUser.getUsername(),
        postLikeRequest.getPostId());
    return ResponseEntity.ok(postLikeResponse);
  }
}
