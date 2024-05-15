package com.allo.server.domain.auth.controller;

import com.allo.server.domain.auth.dto.request.SocialSignUpRequest;
import com.allo.server.domain.auth.dto.request.UserSignUpRequest;
import com.allo.server.domain.auth.service.AuthService;
import com.allo.server.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.allo.server.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping(value = "/users/signUp",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> userSignUp(
        @RequestPart (value = "UserSignUpRequest") UserSignUpRequest request,
        @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        authService.userSignUp(request, multipartFile);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

    @Operation(summary = "소셜 로그인 회원가입")
    @PatchMapping(value = "/users/signUp/social",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> userSocialSignUp(
        @AuthenticationPrincipal UserDetails loginUser,
        @Valid @RequestPart (value = "SocialSignUpRequest") SocialSignUpRequest socialSignUpRequest,
        @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {

        authService.userSocialSignUp(loginUser.getUsername(), socialSignUpRequest, multipartFile);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

}
