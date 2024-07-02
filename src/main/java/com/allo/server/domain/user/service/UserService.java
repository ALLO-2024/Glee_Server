package com.allo.server.domain.user.service;


import com.allo.server.domain.user.dto.request.NicknameRequest;
import com.allo.server.domain.user.dto.request.UserMyProfileRequest;
import com.allo.server.domain.user.dto.response.NicknameResponse;
import com.allo.server.domain.user.dto.response.UserGetProfileResponse;
import com.allo.server.domain.user.entity.Language;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.ErrorCode;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.global.s3.S3Service;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.allo.server.error.ErrorCode.ALREADY_EXIST_NICKNAME;
import static com.allo.server.error.ErrorCode.IMAGE_UPLOAD_FAILD;
import static com.allo.server.error.ErrorCode.UNKNOWN_LANGUAGE;
import static com.allo.server.error.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public NicknameResponse isNicknameDuplicated(@Valid NicknameRequest nickNameRequest) {
        Boolean isDuplicated = userRepository.existsByNickname(nickNameRequest.nickname());
        NicknameResponse response = NicknameResponse.of(isDuplicated);
        return response;
    }

    @Transactional
    public void userMyProfile(String email, UserMyProfileRequest userMyProfileRequest, MultipartFile multipartFile) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));
        String curNickname = userEntity.getNickname();
        String nickname = userMyProfileRequest.nickname();
        String profileImageUrl = userMyProfileRequest.profileImageUrl();
        String language = userMyProfileRequest.language();

        try {
            Language.valueOf(language);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(UNKNOWN_LANGUAGE);
        }

        // 프로필 이미지 교체
        if (multipartFile != null) {
            try {
                CompletableFuture<URL> future = s3Service.uploadFile(multipartFile);
                profileImageUrl = future.thenApply(URL::toString).join();
                userEntity.updateProfileImage(profileImageUrl);
            } catch (IOException e) {
                throw new BadRequestException(IMAGE_UPLOAD_FAILD);
            }
        }

        // 이전 닉네임이 아니라면 중복검사
        if (curNickname.equals(nickname)) {
            userEntity.updateNicknameAndLanguage(nickname, language);
        } else {
            if (userRepository.existsByNickname(nickname)) {
                throw new BadRequestException(ALREADY_EXIST_NICKNAME);
            }

            userEntity.updateNicknameAndLanguage(nickname, language);
        }
    }

    public UserGetProfileResponse getMyProfile(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        UserGetProfileResponse profile = UserGetProfileResponse.of(userEntity.getProfileImageUrl(), userEntity.getNickname());
        return profile;
    }

}
