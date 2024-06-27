package com.allo.server.domain.comment.dto.response;

public record CommentSaveResponse(String nickname, String profileImageUrl, String content, String createdAt) {

    public CommentSaveResponse(String nickname, String profileImageUrl, String content, String createdAt) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.createdAt = createdAt;
    }
}