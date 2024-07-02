package com.allo.server.domain.user.controller;

import com.allo.server.domain.user.dto.request.NicknameRequest;
import com.allo.server.domain.user.dto.request.UserMyProfileRequest;
import com.allo.server.domain.user.dto.response.NicknameResponse;
import com.allo.server.domain.user.dto.response.UserGetProfileResponse;
import com.allo.server.domain.user.service.UserService;
import com.allo.server.response.BaseResponse;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.allo.server.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "닉네임 중복 확인", description = "desc test")
    @PostMapping("/nickname/isDuplicated")
    public ResponseEntity<BaseResponse<NicknameResponse>> isNicknameDuplicated(@RequestBody @Valid NicknameRequest request) {
        NicknameResponse response = userService.isNicknameDuplicated(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @PatchMapping(value = "/my/profile",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> userMyProfile(
        @AuthenticationPrincipal UserDetails loginUser,
        @RequestPart(value = "userMyProfileRequest", required = true) @Valid UserMyProfileRequest userMyProfileRequest,
        @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        userService.userMyProfile(loginUser.getUsername(), userMyProfileRequest, multipartFile);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

    @Operation(summary = "회원 정보 API")
    @GetMapping("/my/profile")
    public ResponseEntity<BaseResponse<UserGetProfileResponse>> getMyProfile(@AuthenticationPrincipal UserDetails loginUser) {
        UserGetProfileResponse response = userService.getMyProfile(loginUser.getUsername());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}
