package com.allo.server.domain.comment.repository.impl;

import com.allo.server.domain.comment.dto.response.CommentGetResponse;
import com.allo.server.domain.comment.dto.response.CommentSaveResponse;
import com.allo.server.domain.comment.repository.CustomCommentRepository;
import com.allo.server.domain.word.dto.response.WordGetResponse;
import com.allo.server.domain.word.repository.CustomWordRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.allo.server.domain.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentSaveResponse> getComments(Long userId, Long postId) {
        List<CommentSaveResponse> response = queryFactory
                .select(Projections.constructor(CommentSaveResponse.class,
                        comment.userEntity.nickname,
                        comment.userEntity.profileImageUrl,
                        comment.content,
                        comment.createdAt.stringValue().substring(0, 16)))
                .from(comment)
                .where(comment.post.postId.eq(postId))
                .orderBy(comment.createdAt.asc())
                .fetch();

        return response;
    }
}