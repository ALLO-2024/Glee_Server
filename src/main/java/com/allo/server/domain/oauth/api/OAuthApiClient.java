package com.allo.server.domain.oauth.api;

import com.allo.server.domain.oauth.dto.response.OAuthInfoResponse;
import com.allo.server.domain.user.entity.SocialType;

public interface OAuthApiClient {
    SocialType socialType();
    OAuthInfoResponse requestOauthInfo(String accessToken);
}
