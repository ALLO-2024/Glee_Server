package com.allo.server.domain.comment.service;

import com.allo.server.domain.comment.dto.request.CommentSaveRequest;
import com.allo.server.domain.comment.dto.response.CommentGetResponse;
import com.allo.server.domain.comment.dto.response.CommentSaveResponse;
import com.allo.server.domain.comment.entity.Comment;
import com.allo.server.domain.comment.repository.CommentRepository;
import com.allo.server.domain.comment.repository.CustomCommentRepository;
import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.post.repository.PostRepository;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import static com.allo.server.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final CustomCommentRepository customCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentSaveResponse saveComment(String email, CommentSaveRequest commentSaveRequest){

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

        return new CommentSaveResponse(userEntity.getUserId(), userEntity.getNickname(), userEntity.getProfileImageUrl(), commentSaveRequest.content(), comment.getCreatedAt().toString().substring(0, 16));
    }

    public List<CommentGetResponse> getComments(String email, Long postId){

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        List<CommentSaveResponse> responses = customCommentRepository.getComments(userEntity.getUserId(), postId);

        return responses.stream()
                .map(response -> new CommentGetResponse(
                        response.nickname(),
                        response.profileImageUrl(),
                        response.content(),
                        response.createdAt(),
                        isCurrentUser(response.userId(), userEntity.getUserId())
                ))
                .collect(Collectors.toList());
    }

    private Boolean isCurrentUser(Long responseUserId, Long userId) {
        return responseUserId.equals(userId);
    }
}
