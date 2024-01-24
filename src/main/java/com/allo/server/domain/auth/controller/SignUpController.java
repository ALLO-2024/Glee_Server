package com.allo.server.domain.auth.controller;

import com.allo.server.domain.auth.dto.request.SocialSignUpRequest;
import com.allo.server.domain.auth.dto.request.UserSignUpRequest;
import com.allo.server.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final AuthService authService;

    @PostMapping("/users/signUp")
    public ResponseEntity<Void> userSignUp(@RequestPart (value = "UserSignUpRequest") UserSignUpRequest request, @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        authService.userSignUp(request, multipartFile);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/signUp/social")
    public ResponseEntity<Void> userSocialSignUp(@AuthenticationPrincipal UserDetails loginUser, @RequestPart (value = "SocialSignUpRequest") SocialSignUpRequest socialSignUpRequest, @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        authService.userSocialSignUp(loginUser.getUsername(), socialSignUpRequest, multipartFile);
        return ResponseEntity.noContent().build();
    }

}
