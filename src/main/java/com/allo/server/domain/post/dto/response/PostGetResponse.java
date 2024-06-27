package com.allo.server.domain.post.dto.response;

import com.allo.server.domain.comment.dto.response.CommentGetResponse;
import com.allo.server.domain.post_image.dto.response.PostImageGetResponse;

import java.util.List;

public record PostGetResponse (PostInfoResponse postInfoResponse, List<PostImageGetResponse> postImageGetResponses, List<CommentGetResponse> commentGetResponses) {
    public static PostGetResponse of(PostInfoResponse postInfoResponse, List<PostImageGetResponse> postImageGetResponses, List<CommentGetResponse> commentGetResponses) {
        return new PostGetResponse(postInfoResponse, postImageGetResponses, commentGetResponses);
    }
}