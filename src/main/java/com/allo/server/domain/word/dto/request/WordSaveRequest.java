package com.allo.server.domain.word.dto.request;

import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.word.entity.Word;
import jakarta.validation.constraints.NotBlank;


public record WordSaveRequest (@NotBlank(message = "단어는 필수 입력 값입니다.")
                               String word,
                               @NotBlank(message = "뜻은 필수 입력 값입니다.")
                               String meaning,
                               @NotBlank(message = "뜻은 필수 입력 값입니다.")
                               String pos,
                               @NotBlank(message = "번역어은 필수 입력 값입니다.")
                               String trans_word,
                               @NotBlank(message = "번역뜻은 필수 입력 값입니다.")
                               String trans_dfn,
                               @NotBlank(message = "예문은 필수 입력 값입니다.")
                               String example
){

    public static Word wordToEntity(UserEntity userEntity, WordSaveRequest wordSaveRequest){
        return Word.builder()
                .userEntity(userEntity)
                .word(wordSaveRequest.word)
                .meaning(wordSaveRequest.meaning)
                .pos(wordSaveRequest.pos)
                .trans_word(wordSaveRequest.trans_word)
                .trans_dfn(wordSaveRequest.trans_dfn)
                .example(wordSaveRequest.example)
                .build();

    }
}
;