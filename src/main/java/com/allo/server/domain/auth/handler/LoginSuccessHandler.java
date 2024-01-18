package com.allo.server.domain.auth.handler;

import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

import static com.allo.server.error.ErrorCode.INVALID_ROLE_NAME;
import static com.allo.server.error.ErrorCode.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private Integer accessTokenExpirationPeriod;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String email = extractUsername(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().toString();
        String roleName = role.substring(6, role.length()-1);

        Long id;
        if ("USER".equals(roleName) || "AUTH_USER".equals(roleName)) {
            id = userRepository.findByEmail(email)
                    .map(UserEntity::getUserId)
                    .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

            roleName = "USER";
        } else {
            throw new BadRequestException(INVALID_ROLE_NAME); // 다른 roleName 들어왔을 경우의 예외 처리
        }

        String accessToken = jwtService.createAccessToken(id, roleName);
        String refreshToken = jwtService.createRefreshToken(id, roleName);
        jwtService.updateRefreshToken(roleName, id, refreshToken);

        // JWT 서비스에서 토큰 정보 가져오기
        Map<String, String> tokenData = jwtService.sendAccessAndRefreshToken(roleName, accessToken, refreshToken);

        // HTTP 응답 설정
        response.setStatus(HttpServletResponse.SC_OK); // 상태 코드를 200 (OK)로 설정
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokenData));
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
