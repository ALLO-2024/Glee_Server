package com.allo.server.domain.post.repository.impl;

import com.allo.server.domain.post.dto.response.PostInfoResponse;
import com.allo.server.domain.post.dto.response.PostListGetResponse;
import com.allo.server.domain.post.repository.CustomPostRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.allo.server.domain.comment.entity.QComment.comment;
import static com.allo.server.domain.post.entity.QPost.post;
import static com.allo.server.domain.post_image.entity.QPostImage.postImage;
import static com.allo.server.domain.post_like.entity.QPostLike.postLike;
import static com.allo.server.error.ErrorCode.INVALID_SORTTYPE;

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
    public List<PostListGetResponse> getPostList(String sortType) {

        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(sortType);

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
                .orderBy(orderSpecifiers)
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

  @Override
  public List<PostListGetResponse> getLikePostList(List<Long> postIdList) {
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
        .where(post.userEntity.userId.in(postIdList))
        .orderBy(post.createdAt.desc())
        .fetch();
  }

    @Override
    public List<PostListGetResponse> searchPostList(String title) {
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
                .where(post.title.contains(title))
                .orderBy(post.createdAt.desc())
                .fetch();

        return response;
    }

    private OrderSpecifier[] createOrderSpecifier(String sortType) {

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        switch (sortType) {
            case "like" :
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, JPAExpressions.select(postLike.count())
                        .from(postLike)
                        .where(postLike.post.postId.eq(post.postId))));
                break;
            case "createdAt":
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, post.createdAt));
                break;
            default:
                throw new BadRequestException(INVALID_SORTTYPE);
        }

        orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, post.createdAt));

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}