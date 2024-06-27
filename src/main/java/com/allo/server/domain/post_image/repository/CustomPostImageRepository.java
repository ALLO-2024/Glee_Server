package com.allo.server.domain.post_image.repository;

import com.allo.server.domain.post_image.dto.response.PostImageGetResponse;

import java.util.List;

public interface CustomPostImageRepository {
    // 게시물 사진 전부 조회
    List<PostImageGetResponse> getPostImages(Long postId);

    // 게시물 사진 1개 조회
    PostImageGetResponse getPostImage(Long postId);
}