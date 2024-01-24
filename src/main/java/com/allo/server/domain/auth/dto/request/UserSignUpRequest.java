package com.allo.server.domain.auth.dto.request;

import com.allo.server.domain.user.entity.Language;
import com.allo.server.domain.user.entity.Role;
import com.allo.server.domain.user.entity.UserEntity;
import jakarta.validation.constraints.*;

public record UserSignUpRequest(@Email(message="이메일 형식에 맞지 않습니다.")
                                     @NotBlank(message = "이메일은 필수 입력 값입니다.") String email,
                                     @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{10,}$|^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=?]).{8,}$",
                                             message = "영문+숫자 10자 이상 또는 영문+숫자+특수기호 8자 이상을 입력해 주세요.") String password,
                                     @NotBlank(message = "닉네임은 필수 입력 값입니다.")
                                     @Size(min=2, max=10, message = "닉네임은 2~10자로 입력해 주세요.")
                                     String nickname,
                                     Boolean isOptionAgr,
                                     @NotBlank(message = "language 필수 입력 값입니다.")
                                     Language language) {

    public UserEntity toEntity(String profileImageUrl) {
        return UserEntity.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .isOptionAgr(isOptionAgr)
                .role(Role.USER)
                .language(language)
                .build();
    }
}

