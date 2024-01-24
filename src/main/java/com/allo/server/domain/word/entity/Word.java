package com.allo.server.domain.word.entity;

import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Word extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    private String word;

    private String meaning;

    private String pos;

    private String trans_word;

    private String trans_dfn;

    @Builder
    public Word(UserEntity userEntity, String word, String meaning, String pos, String trans_word, String trans_dfn) {
        this.userEntity = userEntity;
        this.word = word;
        this.meaning = meaning;
        this.pos = pos;
        this.trans_word = trans_word;
        this.trans_dfn = trans_dfn;
    }
}
