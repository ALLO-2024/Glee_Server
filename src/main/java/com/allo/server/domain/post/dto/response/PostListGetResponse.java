package com.allo.server.domain.post.dto.response;

public record PostListGetResponse (String nickname,
                                 String profileImageUrl,
                                 String createdAt,
                                 Long postId,
                                 String title,
                                 String content,
                                 String postImageUrl,
                                 Long likeCount,
                                 Long commentCount) {
    public static PostListGetResponse of(String nickname,
                                         String profileImageUrl,
                                         String createdAt,
                                         Long postId,
                                         String title,
                                         String content,
                                         String postImageUrl,
                                         Long likeCount,
                                         Long commentCount) {
        return new PostListGetResponse(nickname, profileImageUrl, createdAt, postId, title, content, postImageUrl, likeCount, commentCount);
    }
}