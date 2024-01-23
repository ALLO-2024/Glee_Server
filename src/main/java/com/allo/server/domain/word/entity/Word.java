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

    private String example;

    @Builder
    public Word(UserEntity userEntity, String word, String meaning, String example) {
        this.userEntity = userEntity;
        this.word = word;
        this.meaning = meaning;
        this.example = example;
    }
}
