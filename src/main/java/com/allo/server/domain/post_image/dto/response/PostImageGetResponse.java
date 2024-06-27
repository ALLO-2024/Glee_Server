package com.allo.server.domain.post_image.dto.response;

public record PostImageGetResponse (String postImageUrl) {
    public static PostImageGetResponse of(String postImageUrl) {
        return new PostImageGetResponse(postImageUrl);
    }
}