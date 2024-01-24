package com.allo.server.domain.word.dto.response;

public record WordSearchResponse(String word, String meaning, String pos, String trans_word, String trans_dfn, String example, Boolean exist) {

    public WordSearchResponse(String word, String meaning, String pos, String trans_word, String trans_dfn, String example, Boolean exist) {
        this.word = word;
        this.meaning = meaning;
        this.pos = pos;
        this.trans_word = trans_word;
        this.trans_dfn = trans_dfn;
        this.example = example;
        this.exist = exist;
    }
}
