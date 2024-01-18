package com.allo.server.domain.oauth.dto.response;

import com.allo.server.domain.user.entity.SocialType;

public interface OAuthInfoResponse {
    SocialType getSocialType();
    String getId();
}
