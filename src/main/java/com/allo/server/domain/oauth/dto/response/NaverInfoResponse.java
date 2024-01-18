package com.allo.server.domain.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.allo.server.domain.user.entity.SocialType;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {
    @JsonProperty("response")
    private Response response;
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {
        private String id; // 소셜 식별 값
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }
    @Override
    public String getId() {
        return response.getId();
    }
}
