package com.allo.server.domain.post_image.dto.response;

public record PostImageGetResponse (Long postImageId,
                                String postImageUrl) {
    public static PostImageGetResponse of(Long postImageId, String postImageUrl) {
        return new PostImageGetResponse(postImageId, postImageUrl);
    }
}