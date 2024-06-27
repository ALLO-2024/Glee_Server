package com.allo.server.domain.comment.service;

import com.allo.server.domain.comment.dto.request.CommentSaveRequest;
import com.allo.server.domain.comment.entity.Comment;
import com.allo.server.domain.comment.repository.CommentRepository;
import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.post.repository.PostRepository;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import static com.allo.server.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void saveComment(String email, CommentSaveRequest commentSaveRequest){

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        Post post = postRepository.findById(commentSaveRequest.postId()).orElseThrow(() -> new BadRequestException(POST_NOT_FOUND));

        Comment parentComment;
        if(commentSaveRequest.parent_id() == null || commentSaveRequest.parent_id().describeConstable().isEmpty())  {
            parentComment = null;
        }
        else {
            parentComment = commentRepository.findById(commentSaveRequest.parent_id()).orElseThrow(() -> new BadRequestException(COMMENT_NOT_FOUND));
        }

        Comment comment = new Comment(userEntity, post, commentSaveRequest.content(), parentComment);
        commentRepository.save(comment);
    }

}
