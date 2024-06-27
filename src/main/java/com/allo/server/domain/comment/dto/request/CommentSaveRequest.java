package com.allo.server.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentSaveRequest (@NotBlank(message = "댓글은 필수 입력 값입니다.")
                               String content,
                               @NotNull(message = "Post_Id은 필수 입력 값입니다.")
                               Long postId,
                               Long parent_id
){
}
;