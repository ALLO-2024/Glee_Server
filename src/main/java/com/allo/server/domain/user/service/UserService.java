package com.allo.server.domain.user.service;


import com.allo.server.domain.user.dto.request.NicknameRequest;
import com.allo.server.domain.user.dto.request.UserMyProfileRequest;
import com.allo.server.domain.user.dto.response.NicknameResponse;
import com.allo.server.domain.user.dto.response.UserGetProfileResponse;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.ErrorCode;
import com.allo.server.error.exception.custom.BadRequestException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.allo.server.error.ErrorCode.ALREADY_EXIST_NICKNAME;
import static com.allo.server.error.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public NicknameResponse isNicknameDuplicated(@Valid NicknameRequest nickNameRequest) {
        Boolean isDuplicated = userRepository.existsByNickname(nickNameRequest.nickname());
        NicknameResponse response = NicknameResponse.of(isDuplicated);
        return response;
    }

    public void userMyProfile(String email, UserMyProfileRequest userMyProfileRequest) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));
        String curNickname = userEntity.getNickname();
        String nickname = userMyProfileRequest.nickname();
        String profileImageUrl = userMyProfileRequest.profileImageUrl();

        if (curNickname.equals(nickname)) {
            userEntity.updateProfileImage(profileImageUrl);
        } else {
            if (userRepository.existsByNickname(nickname)) {
                throw new BadRequestException(ALREADY_EXIST_NICKNAME);
            }

            userEntity.updateMyProfile(nickname, profileImageUrl);
        }
    }

    public UserGetProfileResponse getMyProfile(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        UserGetProfileResponse profile = UserGetProfileResponse.of(userEntity.getProfileImageUrl(), userEntity.getNickname());
        return profile;
    }

}
