package com.allo.server.domain.word.dto.request;

import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.word.entity.Word;
import jakarta.validation.constraints.NotBlank;


public record WordSaveRequest (@NotBlank(message = "단어는 필수 입력 값입니다.")
                                  String word
){

    public static Word wordToEntity(UserEntity userEntity, WordSaveRequest wordSaveRequest, String meaning, String example){
        return Word.builder()
                .userEntity(userEntity)
                .word(wordSaveRequest.word)
                .meaning(meaning)
                .example(example)
                .build();

    }
}
;