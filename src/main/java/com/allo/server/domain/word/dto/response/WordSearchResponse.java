package com.allo.server.domain.word.dto.response;

public record WordSearchResponse(String word, String meaning, String example, Boolean exist) {

    public WordSearchResponse(String word, String meaning, String example, Boolean exist) {
        this.word = word;
        this.meaning = meaning;
        this.example = example;
        this.exist = exist;
    }
}
