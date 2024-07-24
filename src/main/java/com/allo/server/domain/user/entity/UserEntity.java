package com.allo.server.domain.user.entity;

import com.allo.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.codec.language.bm.Lang;
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

    @Enumerated(EnumType.STRING)
    private Language language; // ENGLISH : 영어, CHINESE : 중국어, JAPANESE : 일본어, VIETNAMESE : 베트남어

    private Boolean isOptionAgr; // 선택 이용약관 체크 여부


    @Builder
    public UserEntity(String email, String password, String nickname, String profileImageUrl, Role role, SocialType socialType, String socialId, Language language, Boolean isOptionAgr) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.language = language;
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

    public void updateSocialUser(String nickname, String profileImageUrl, Role role, Language language, Boolean isOptionAgr) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.language = language;
        this.isOptionAgr = isOptionAgr;
    }

    public void updateNicknameAndLanguage(String nickname, String language) {
        this.nickname = nickname;
        this.language = Language.valueOf(language);
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}

