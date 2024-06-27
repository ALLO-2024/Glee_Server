package com.allo.server.domain.post.dto.response;

public record PostInfoResponse (String nickname,
                                String profileImageUrl,
                                String createdAt,
                                String title,
                                String content,
                                Long likeCount,
                                Long commentCount) {
    public static PostInfoResponse of(String nickname,
                                      String profileImageUrl,
                                      String createdAt,
                                      String title,
                                      String content,
                                      Long likeCount,
                                      Long commentCount) {
        return new PostInfoResponse(nickname, profileImageUrl, createdAt, title, content, likeCount, commentCount);
    }
}