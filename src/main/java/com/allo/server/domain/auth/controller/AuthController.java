package com.allo.server.domain.auth.controller;

import com.allo.server.domain.auth.dto.request.RefreshTokenRequest;
import com.allo.server.domain.auth.service.AuthService;
import com.allo.server.domain.oauth.dto.response.LoginResponse;
import com.allo.server.jwt.service.JwtService;
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

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/reissue-token")
    public ResponseEntity<LoginResponse> reIssueToken(@Valid @RequestBody RefreshTokenRequest request) {

        String refreshToken = request.refreshToken();
        LoginResponse response = jwtService.reIssueToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/logout")
    public ResponseEntity<Void> userLogout(HttpServletRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        authService.userLogout(request, loginUser.getUsername());
        return ResponseEntity.noContent().build();
    }


}
