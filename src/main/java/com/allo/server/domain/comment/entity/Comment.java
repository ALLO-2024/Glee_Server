package com.allo.server.domain.comment.entity;

import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity userEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Column(columnDefinition = "TEXT")
  private String content;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Comment parentComment; //부모 댓글

  @Builder
  public Comment(UserEntity userEntity, Post post, String content, Comment parentComment) {
    this.userEntity = userEntity;
    this.post = post;
    this.content = content;
    this.parentComment = parentComment;
  }
}
