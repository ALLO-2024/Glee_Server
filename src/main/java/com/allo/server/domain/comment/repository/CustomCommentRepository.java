package com.allo.server.domain.comment.repository;

import com.allo.server.domain.comment.dto.response.CommentSaveResponse;

import java.util.List;

public interface CustomCommentRepository {
    // 댓글 조회
    List<CommentSaveResponse> getComments(Long postId);
}