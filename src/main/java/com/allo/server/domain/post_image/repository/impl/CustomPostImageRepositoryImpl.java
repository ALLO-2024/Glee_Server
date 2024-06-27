package com.allo.server.domain.post_image.repository.impl;

import com.allo.server.domain.comment.dto.response.CommentSaveResponse;
import com.allo.server.domain.comment.repository.CustomCommentRepository;
import com.allo.server.domain.post_image.dto.response.PostImageGetResponse;
import com.allo.server.domain.post_image.repository.CustomPostImageRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.allo.server.domain.post_image.entity.QPostImage.postImage;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomPostImageRepositoryImpl implements CustomPostImageRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostImageGetResponse> getPostImages(Long postId) {
        List<PostImageGetResponse> response = queryFactory
                .select(Projections.constructor(PostImageGetResponse.class,
                        postImage.postImageUrl))
                .from(postImage)
                .where(postImage.post.postId.eq(postId))
                .orderBy(postImage.createdAt.asc())
                .fetch();

        return response;
    }

    @Override
    public PostImageGetResponse getPostImage(Long postId){
        PostImageGetResponse response = queryFactory
                .select(Projections.constructor(PostImageGetResponse.class,
                        postImage.postImageUrl))
                .from(postImage)
                .where(postImage.post.postId.eq(postId))
                .orderBy(postImage.createdAt.asc())
                .fetchOne();

        return response;
    }
}