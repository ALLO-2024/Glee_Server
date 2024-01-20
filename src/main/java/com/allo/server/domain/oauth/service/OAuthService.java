package com.allo.server.domain.oauth.service;

import com.allo.server.domain.oauth.dto.request.SocialLoginRequest;
import com.allo.server.domain.oauth.dto.response.LoginResponse;
import com.allo.server.domain.oauth.dto.response.OAuthInfoResponse;
import com.allo.server.domain.user.entity.Role;
import com.allo.server.domain.user.entity.SocialType;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.allo.server.error.ErrorCode.USER_NOT_FOUND;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtService jwtService;

    public LoginResponse userSocialLogin(SocialLoginRequest request) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(request);
        SocialType socialType = oAuthInfoResponse.getSocialType();
        String socialId = oAuthInfoResponse.getId();
        String socialEmail = oAuthInfoResponse.getEmail();

        Long id = findOrSaveUser(socialType, socialId, socialEmail); // User id 반환

        String role = String.valueOf(userRepository.findById(id)
                .map(UserEntity::getRole)
                .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND)));

        String roleName = "USER";
        String accessToken = jwtService.createAccessToken(id, roleName);
        String refreshToken = jwtService.createRefreshToken(id, roleName);
//        jwtService.updateRefreshToken(roleName, id, refreshToken);

        return LoginResponse.of(role, accessToken, refreshToken);
    }

    private Long findOrSaveUser(SocialType socialType, String socialId, String email) {

        Optional<UserEntity> userEntity = userRepository.findBySocialTypeAndSocialId(socialType, socialId);

        if (userEntity.isPresent()) {
            return userEntity.get().getUserId();
        } else {
            return saveUser(socialType, socialId, email);
        }
    }

    private Long saveUser(SocialType socialType, String socialId, String email) {
        UserEntity userEntity = new UserEntity(email, Role.USER, socialType, socialId);

        return userRepository.save(userEntity).getUserId();
    }
}
