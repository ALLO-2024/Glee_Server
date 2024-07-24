package com.allo.server.domain.comment.dto.response;

import java.util.List;

public record CommentGetResponse(Long commentId, String nickname, String profileImageUrl, String content, String createdAt, Boolean now, Long parentCommentId, List<CommentGetResponse> childResponses) {

    public CommentGetResponse(Long commentId, String nickname, String profileImageUrl, String content, String createdAt, Boolean now, Long parentCommentId, List<CommentGetResponse> childResponses) {
        this.commentId = commentId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.createdAt = createdAt;
        this.now = now;
        this.parentCommentId = parentCommentId;
        this.childResponses = childResponses;
    }
}