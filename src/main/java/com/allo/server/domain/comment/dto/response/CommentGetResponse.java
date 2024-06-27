package com.allo.server.domain.comment.dto.response;

public record CommentGetResponse(String nickname, String profileImageUrl, String content, String createdAt, Boolean now) {

    public CommentGetResponse(String nickname, String profileImageUrl, String content, String createdAt, Boolean now) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.createdAt = createdAt;
        this.now = now;
    }
}