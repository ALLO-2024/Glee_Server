package com.allo.server.domain.post.repository;

import com.allo.server.domain.post.dto.response.PostInfoResponse;
import com.allo.server.domain.post.dto.response.PostListGetResponse;

import java.util.List;

public interface CustomPostRepository {

    // 게시물 정보 조회
    PostInfoResponse getPost(Long userId, Long postId);

    // 게시물 목록 조회
    List<PostListGetResponse> getPostList();

    // 내가 쓴 게시물 조회
    List<PostListGetResponse> getMyPostList(Long userId);

    // 좋아요한 게시물 조회
    List<PostListGetResponse> getLikePostList(List<Long> postIdList);
}