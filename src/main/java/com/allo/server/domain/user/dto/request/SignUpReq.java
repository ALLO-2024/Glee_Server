package com.allo.server.domain.user.dto.request;

import com.allo.server.domain.user.entity.Role;
import com.allo.server.domain.user.entity.UserEntity;
import lombok.Getter;

@Getter
public class SignUpReq {

    private String name;
    private String email;
    private String password;

    public UserEntity toEntity(String encPwd) {
        return UserEntity.builder()
                .name(name)
                .email(email)
                .password(encPwd)
                .role(Role.USER)
                .build();
    }
}
