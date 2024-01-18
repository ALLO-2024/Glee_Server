package com.allo.server.domain.user.entity;

import com.allo.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    @Column(length = 15)
    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER

    private String socialId; // 로그인한 소셜 타입 식별자 값 (일반 로그인의 경우 null)

    private Boolean isOptionAgr; // 선택 이용약관 체크 여부


    @Builder
    public UserEntity(String email, String password, String nickname, String profileImageUrl, Role role, SocialType socialType, String socialId, Boolean isOptionAgr) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.isOptionAgr = isOptionAgr;
    }

    public UserEntity(String email, Role role, SocialType socialType, String socialId) {
        this.email = email;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateSocialUser(String nickname, Role role, Boolean isOptionAgr) {
        this.nickname = nickname;
        this.role = role;
        this.isOptionAgr = isOptionAgr;
    }

    public void updateMyProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}

