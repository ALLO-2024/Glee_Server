package com.allo.server.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank(message = "RefreshToken은 필수 입력 값입니다.")
                                  String refreshToken) {
}
