package com.allo.server.domain.user.dto.response;

public record UserGetProfileResponse(String profileImageUrl, String nickname) {
    public static UserGetProfileResponse of(String profileImageUrl, String nickname) {
        return new UserGetProfileResponse(profileImageUrl, nickname);
    }
}