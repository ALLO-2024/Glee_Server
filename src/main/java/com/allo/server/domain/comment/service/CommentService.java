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


import java.util.*;
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
        if(commentSaveRequest.parentId() == null || commentSaveRequest.parentId().describeConstable().isEmpty())  {
            parentComment = null;
        }
        else {
            parentComment = commentRepository.findById(commentSaveRequest.parentId()).orElseThrow(() -> new BadRequestException(COMMENT_NOT_FOUND));
        }

        Comment comment = new Comment(userEntity, post, commentSaveRequest.content(), parentComment);
        commentRepository.save(comment);

        return new CommentSaveResponse(comment.getCommentId(), userEntity.getUserId(), userEntity.getNickname(), userEntity.getProfileImageUrl(), commentSaveRequest.content(), comment.getCreatedAt().toString().substring(0, 16), parentComment != null ? parentComment.getCommentId() : null);
    }

    public List<CommentGetResponse> getComments(String email, Long postId) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        List<CommentSaveResponse> responses = customCommentRepository.getComments(postId);

        // parentCommentId 기준으로 자식 Comment 그룹화
        Map<Long, List<CommentGetResponse>> childCommentsMap = responses.stream()
                // parentCommentId 가 null 이 아닐 경우만 stream 에서 추출
                .filter(response -> response.parentCommentId() != null)
                // CommentSaveResponse 의 parentCommentId 를 기준으로 그룹화
                .collect(Collectors.groupingBy(
                        CommentSaveResponse::parentCommentId,
                        Collectors.mapping(response -> new CommentGetResponse(
                                response.commentId(),
                                response.nickname(),
                                response.profileImageUrl(),
                                response.content(),
                                response.createdAt(),
                                isCurrentUser(response.userId(), userEntity.getUserId()),
                                response.parentCommentId(),
                                Collections.emptyList() // 초기 자식 목록은 비어 있음
                        ), Collectors.toList()) // 추출된 요소들을 list 로 변환
                ));

        // 이미 포함된 댓글 ID를 추적하는 집합
        Set<Long> includedComments = new HashSet<>();

        return responses.stream()
                .map(response -> {
                    // 이미 포함된 댓글은 무시
                    if (includedComments.contains(response.commentId())) {
                        return null;
                    }

                    // 자식 Comment 목록
                    List<CommentGetResponse> childResponses = childCommentsMap.getOrDefault(response.commentId(), Collections.emptyList());
                    includedComments.add(response.commentId());
                    includedComments.addAll(childResponses.stream().map(CommentGetResponse::commentId).collect(Collectors.toList())); // 자식 CommentId 추가

                    return new CommentGetResponse(
                            response.commentId(),
                            response.nickname(),
                            response.profileImageUrl(),
                            response.content(),
                            response.createdAt(),
                            isCurrentUser(response.userId(), userEntity.getUserId()),
                            response.parentCommentId(),
                            childResponses // 추가된 대댓글 목록
                    );
                })
                .filter(Objects::nonNull) // null 값 제거
                .collect(Collectors.toList());
    }

    // 현재 사용자인지 여부를 판단
    private Boolean isCurrentUser(Long responseUserId, Long userId) {
        return responseUserId.equals(userId);
    }
}
