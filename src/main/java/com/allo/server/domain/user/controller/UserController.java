package com.allo.server.domain.user.controller;

import com.allo.server.domain.user.dto.request.NicknameRequest;
import com.allo.server.domain.user.dto.request.UserMyProfileRequest;
import com.allo.server.domain.user.dto.response.NicknameResponse;
import com.allo.server.domain.user.dto.response.UserGetProfileResponse;
import com.allo.server.domain.user.service.UserService;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "닉네임 중복 확인", description = "desc test")
    @PostMapping("/nickname/isDuplicated")
    public ResponseEntity<NicknameResponse> isNicknameDuplicated(@RequestBody @Valid NicknameRequest request) {
        NicknameResponse response = userService.isNicknameDuplicated(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/my/profile")
    public ResponseEntity<Void> userMyProfile(@AuthenticationPrincipal UserDetails loginUser, @RequestBody @Valid UserMyProfileRequest userMyProfileRequest) {
        userService.userMyProfile(loginUser.getUsername(), userMyProfileRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my/profile")
    public ResponseEntity<UserGetProfileResponse> getMyProfile(@AuthenticationPrincipal UserDetails loginUser) {
        UserGetProfileResponse response = userService.getMyProfile(loginUser.getUsername());
        return ResponseEntity.ok(response);
    }
}
