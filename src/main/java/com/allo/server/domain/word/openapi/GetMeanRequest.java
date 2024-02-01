package com.allo.server.domain.word.openapi;

import com.allo.server.domain.user.entity.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetMeanRequest {
    private String key;
    private String q;
    private String part;
    private String translated;
    private String method;
    private String trans_lang;

    public GetMeanRequest(String key, String q, Language language) {
        this.key = key;
        this.q = q;
        this.part = "word";
        this.translated = "y";
        this.trans_lang = language.getKey();
        this.method = "exact";
    }

    public String getParameter() {
        return "?key=" + this.key +
                "&q=" + this.q +
                "&part=" + this.part +
                "&translated=" + this.translated +
                "&trans_lang=" + this.trans_lang +
                "&method=" + this.method;
    }
}