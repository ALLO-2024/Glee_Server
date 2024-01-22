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

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final AuthService authService;

    @PostMapping("/users/signUp")
    public ResponseEntity<Void> userSignUp(@RequestBody @Valid UserSignUpRequest request) {
        authService.userSignUp(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/signUp/social")
    public ResponseEntity<Void> userSocialSignUp(@AuthenticationPrincipal UserDetails loginUser, @RequestBody @Valid SocialSignUpRequest socialSignUpRequest) {
        authService.userSocialSignUp(loginUser.getUsername(), socialSignUpRequest);
        return ResponseEntity.noContent().build();
    }

}
