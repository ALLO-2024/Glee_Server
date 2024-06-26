package com.allo.server.domain.post.dto.request;

import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;

public record PostSaveRequest (@NotNull(message = "제목은 필수 입력 값입니다.")
                               String title,
                               @NotNull(message = "뜻은 필수 입력 값입니다.")
                               String content
){

    public static Post postToEntity(UserEntity userEntity, PostSaveRequest postSaveRequest){
        return Post.builder()
                .userEntity(userEntity)
                .title(postSaveRequest.title)
                .content(postSaveRequest.content)
                .build();

    }
}
;