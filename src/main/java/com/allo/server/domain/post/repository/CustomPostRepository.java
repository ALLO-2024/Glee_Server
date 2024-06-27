package com.allo.server.domain.post.repository;

import com.allo.server.domain.post.dto.response.PostInfoResponse;

public interface CustomPostRepository {

    // 게시물 정보 조회
    public PostInfoResponse getPost(Long userId, Long postId);
}