package com.allo.server.domain.oauth.dto.request;

import com.allo.server.domain.user.entity.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SocialLoginCodeRequest(
        @NotBlank(message = "AccessToken은 필수 입력 값입니다.")
        String code,
        @NotNull(message = "provider는 필수 입력 값입니다.")
        SocialType provider) {
}