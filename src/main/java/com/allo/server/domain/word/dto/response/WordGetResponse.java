package com.allo.server.domain.word.dto.response;

import com.allo.server.domain.word.entity.Word;

public record WordGetResponse(Long wordId, String word, String meaning, String pos, String trans_word, String example) {

    public WordGetResponse(Long wordId, String word, String meaning, String pos, String trans_word, String example) {
        this.wordId = wordId;
        this.word = word;
        this.meaning = meaning;
        this.pos = pos;
        this.trans_word = trans_word;
        this.example = example;
    }

}