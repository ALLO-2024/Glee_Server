package com.allo.server.domain.post.dto.response;

public record PostInfoResponse (Long userId,
                                String nickname,
                                String profileImageUrl,
                                String createdAt,
                                String title,
                                String content,
                                Integer likeCount,
                                Integer commentCount) {
    public static PostInfoResponse of(Long userId,
                                      String nickname,
                                      String profileImageUrl,
                                      String createdAt,
                                      String title,
                                      String content,
                                      Integer likeCount,
                                      Integer commentCount) {
        return new PostInfoResponse(userId, nickname, profileImageUrl, createdAt, title, content, likeCount, commentCount);
    }
}