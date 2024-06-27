package com.allo.server.domain.comment.dto.response;

public record CommentSaveResponse(Long userId, String nickname, String profileImageUrl, String content, String createdAt) {

    public CommentSaveResponse(Long userId, String nickname, String profileImageUrl, String content, String createdAt) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.createdAt = createdAt;
    }
}