package com.allo.server.domain.comment.dto.response;

public record CommentSaveResponse(Long commentId, Long userId, String nickname, String profileImageUrl, String content, String createdAt, Long parentCommentId) {

    public CommentSaveResponse(Long commentId, Long userId, String nickname, String profileImageUrl, String content, String createdAt, Long parentCommentId) {
        this.commentId = commentId;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.createdAt = createdAt;
        this.parentCommentId = parentCommentId;
    }
}