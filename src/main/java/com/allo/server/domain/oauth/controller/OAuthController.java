package com.allo.server.domain.oauth.controller;

import com.allo.server.domain.oauth.dto.request.SocialLoginRequest;
import com.allo.server.domain.oauth.dto.response.LoginResponse;
import com.allo.server.domain.oauth.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @Operation(summary = "소셜 로그인 API")
    @PostMapping("/users/login/social")
    public ResponseEntity<LoginResponse> userSocialLogin(@RequestBody @Valid SocialLoginRequest request) {
        LoginResponse response = oAuthService.userSocialLogin(request);
        return ResponseEntity.ok(response);
    }

}
