package com.allo.server.domain.oauth.provider;

public interface OAuthProvider {
    String getAccessToken(String clientId, String clientSecret, String redirectUri, String code);
}