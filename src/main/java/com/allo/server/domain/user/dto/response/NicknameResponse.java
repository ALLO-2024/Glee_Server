package com.allo.server.domain.user.dto.response;

public record NicknameResponse(Boolean isDuplicated) {
    public static NicknameResponse of(Boolean isDuplicated) { return new NicknameResponse(isDuplicated);}
}
