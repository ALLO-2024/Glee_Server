package com.allo.server.domain.oauth.controller;

import com.allo.server.domain.oauth.dto.request.SocialLoginCodeRequest;
import com.allo.server.domain.oauth.dto.request.SocialLoginRequest;
import com.allo.server.domain.oauth.dto.response.LoginResponse;
import com.allo.server.domain.oauth.service.OAuthService;
import com.allo.server.response.BaseResponse;
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
    public ResponseEntity<BaseResponse<LoginResponse>> userSocialLogin(@RequestBody @Valid SocialLoginCodeRequest codeRequest) {
        String accessToken = oAuthService.getKakaoAccessToken(codeRequest.code());
        SocialLoginRequest request = new SocialLoginRequest(accessToken, codeRequest.provider());
        LoginResponse response = oAuthService.userSocialLogin(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

}
