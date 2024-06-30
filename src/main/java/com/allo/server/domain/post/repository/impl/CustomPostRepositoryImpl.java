package com.allo.server.domain.post.repository.impl;

import com.allo.server.domain.post.dto.response.PostInfoResponse;
import com.allo.server.domain.post.dto.response.PostListGetResponse;
import com.allo.server.domain.post.repository.CustomPostRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.allo.server.domain.comment.entity.QComment.comment;
import static com.allo.server.domain.post.entity.QPost.post;
import static com.allo.server.domain.post_image.entity.QPostImage.postImage;
import static com.allo.server.domain.post_like.entity.QPostLike.postLike;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public PostInfoResponse getPost(Long userId, Long postId) {
        PostInfoResponse response = queryFactory
                .select(Projections.constructor(PostInfoResponse.class,
                        post.userEntity.nickname,
                        post.userEntity.profileImageUrl,
                        post.createdAt.stringValue().substring(0, 16),
                        post.title,
                        post.content,
                        JPAExpressions.select(postLike.count())
                                .from(postLike)
                                .where(postLike.post.postId.eq(post.postId)),
                        JPAExpressions.select(comment.count())
                                .from(comment)
                                .where(comment.post.postId.eq(post.postId)),
                        JPAExpressions.selectOne()
                                .from(postLike)
                                .where(postLike.userEntity.userId.eq(userId)
                                        .and(postLike.post.postId.eq(postId)))
                                .exists()
                ))
                .from(post)
                .where(post.postId.eq(postId))
                .fetchOne();

        return response;
    }

    @Override
    public List<PostListGetResponse> getPostList() {
        List<PostListGetResponse> response = queryFactory
                .select(Projections.constructor(PostListGetResponse.class,
                        post.userEntity.nickname,
                        post.userEntity.profileImageUrl,
                        post.createdAt.stringValue().substring(0, 16),
                        post.title,
                        post.content,
                        JPAExpressions.select(postImage.postImageUrl)
                                .from(postImage)
                                .where(postImage.post.postId.eq(post.postId)
                                        .and(postImage.postImageId.eq(
                                                JPAExpressions.select(postImage.postImageId.min())
                                                        .from(postImage)
                                                        .where(postImage.post.postId.eq(post.postId))
                                        )))
                                .limit(1),
                        JPAExpressions.select(postLike.count())
                                .from(postLike)
                                .where(postLike.post.postId.eq(post.postId)),
                        JPAExpressions.select(comment.count())
                                .from(comment)
                                .where(comment.post.postId.eq(post.postId))
                ))
                .from(post)
                .orderBy(post.createdAt.desc())
                .fetch();

        return response;
    }

  @Override
  public List<PostListGetResponse> getMyPostList(Long userId) {
    return queryFactory
        .select(Projections.constructor(PostListGetResponse.class,
            post.userEntity.nickname,
            post.userEntity.profileImageUrl,
            post.createdAt.stringValue().substring(0, 16),
            post.title,
            post.content,
            JPAExpressions.select(postImage.postImageUrl)
                .from(postImage)
                .where(postImage.post.postId.eq(post.postId)
                    .and(postImage.postImageId.eq(
                        JPAExpressions.select(postImage.postImageId.min())
                            .from(postImage)
                            .where(postImage.post.postId.eq(post.postId))
                    )))
                .limit(1),
            JPAExpressions.select(postLike.count())
                .from(postLike)
                .where(postLike.post.postId.eq(post.postId)),
            JPAExpressions.select(comment.count())
                .from(comment)
                .where(comment.post.postId.eq(post.postId))
        ))
        .from(post)
        .where(post.userEntity.userId.eq(userId))
        .orderBy(post.createdAt.desc())
        .fetch();
  }
}