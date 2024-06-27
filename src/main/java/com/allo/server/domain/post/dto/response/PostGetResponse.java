package com.allo.server.domain.post.dto.response;

import com.allo.server.domain.post_image.dto.response.PostImageGetResponse;

import java.util.List;

public record PostGetResponse (PostInfoResponse postInfoResponse, List<PostImageGetResponse> postImageGetResponses) {
    public static PostGetResponse of(PostInfoResponse postInfoResponse, List<PostImageGetResponse> postImageGetResponses) {
        return new PostGetResponse(postInfoResponse, postImageGetResponses);
    }
}