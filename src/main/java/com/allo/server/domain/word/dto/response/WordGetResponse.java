package com.allo.server.domain.word.dto.response;

public record WordGetResponse(String word, String meaning, String pos, String trans_word, String example) {

    public WordGetResponse(String word, String meaning, String pos, String trans_word, String example) {
        this.word = word;
        this.meaning = meaning;
        this.pos = pos;
        this.trans_word = trans_word;
        this.example = example;
    }
}