package com.allo.server.domain.word.dto.request;

import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.word.entity.Word;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record WordSaveRequest (@NotNull(message = "단어는 필수 입력 값입니다.")
                               String word,
                               @NotNull(message = "뜻은 필수 입력 값입니다.")
                               String meaning,
                               @NotNull(message = "뜻은 필수 입력 값입니다.")
                               String pos,
                               @NotNull(message = "번역어은 필수 입력 값입니다.")
                               String trans_word,
                               @NotNull(message = "예문은 필수 입력 값입니다.")
                               String example
){

    public static Word wordToEntity(UserEntity userEntity, WordSaveRequest wordSaveRequest){
        return Word.builder()
                .userEntity(userEntity)
                .word(wordSaveRequest.word)
                .meaning(wordSaveRequest.meaning)
                .pos(wordSaveRequest.pos)
                .trans_word(wordSaveRequest.trans_word)
                .example(wordSaveRequest.example)
                .build();

    }
}
;