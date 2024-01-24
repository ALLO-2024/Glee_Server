package com.allo.server.domain.word.openapi;

import com.allo.server.domain.user.entity.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetExampleRequest {
    private String key;
    private String q;
    private String part;
    private String translated;
    private String method;
    private String trans_lang;

    public GetExampleRequest(String key, String q) {
        this.key = key;
        this.q = q;
        this.part = "exam";
        this.translated = "n";
        this.method = "exact";
    }

    public String getParameter() {
        return "?key=" + this.key +
                "&q=" + this.q +
                "&part=" + this.part +
                "&translated=" + this.translated +
                "&method=" + this.method;
    }
}