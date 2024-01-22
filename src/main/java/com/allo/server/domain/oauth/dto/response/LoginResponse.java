package com.allo.server.domain.oauth.dto.response;

public record LoginResponse(String roleName, String accessToken, String refreshToken) {
    public static LoginResponse of(String roleName, String accessToken, String refreshToken){
        return new LoginResponse(roleName, accessToken, refreshToken);
    }
}
