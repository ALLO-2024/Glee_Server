package com.allo.server.domain.auth.controller;

import com.allo.server.domain.auth.dto.request.RefreshTokenRequest;
import com.allo.server.domain.auth.service.AuthService;
import com.allo.server.domain.oauth.dto.response.LoginResponse;
import com.allo.server.jwt.service.JwtService;
import com.allo.server.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.allo.server.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/reissue-token")
    public ResponseEntity<BaseResponse<LoginResponse>> reIssueToken(@Valid @RequestBody RefreshTokenRequest request) {

        String refreshToken = request.refreshToken();
        LoginResponse response = jwtService.reIssueToken(refreshToken);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @DeleteMapping("/users/logout")
    public ResponseEntity<BaseResponse> userLogout(HttpServletRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        authService.userLogout(request, loginUser.getUsername());
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }


}
