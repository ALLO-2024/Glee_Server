package com.allo.server.domain.post.repository;

import com.allo.server.domain.post.dto.response.PostInfoResponse;

import java.util.List;

public interface CustomPostRepository {

    // 게시물 정보 조회
    PostInfoResponse getPost(Long postId);
}