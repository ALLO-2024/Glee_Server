package com.allo.server.domain.auth.service;

import com.allo.server.domain.auth.dto.request.SocialSignUpRequest;
import com.allo.server.domain.auth.dto.request.UserSignUpRequest;
import com.allo.server.domain.user.entity.Role;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.global.s3.S3Service;
import com.allo.server.jwt.service.JwtService;
import com.allo.server.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.allo.server.error.ErrorCode.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final JwtService jwtService;
    private final RedisUtil redisUtil;

    public void userSignUp(UserSignUpRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException(ALREADY_EXIST_EMAIL);
        }
        if (userRepository.existsByNickname(request.nickname())) {
            throw new BadRequestException(ALREADY_EXIST_NICKNAME);
        }

        UserEntity userEntity = request.toEntity();
        userEntity.passwordEncode(passwordEncoder);
        userRepository.save(userEntity);
    }


    public void userSocialSignUp(String email, SocialSignUpRequest socialSignUpRequest) {

        if (userRepository.existsByNickname(socialSignUpRequest.nickname())) {
            throw new BadRequestException(ALREADY_EXIST_NICKNAME);
        }

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        // 추가 정보 업데이트
        String nickname = socialSignUpRequest.nickname();
        Boolean isOptionAgr = socialSignUpRequest.isOptionAgr();
        userEntity.updateSocialUser(nickname, Role.USER, isOptionAgr);
    }

    public void userLogout(HttpServletRequest request, String email) {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(() -> new BadRequestException(TOKEN_NOT_EXIST));
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        String roleName = jwtService.extractRoleName(accessToken).orElseThrow(() -> new BadRequestException(NOT_FOUND_ROLE_NAME));

        redisUtil.delete(roleName, userEntity.getUserId());
        redisUtil.setBlackList(accessToken, "accessToken", jwtService.getAccessTokenExpirationPeriod());
    }

}
