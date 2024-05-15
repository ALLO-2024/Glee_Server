package com.allo.server.jwt;

import com.allo.server.error.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.allo.server.error.ErrorCode.NOT_AUTHENTICATED_REQUEST;

/**
 * 유효한 자격 증명을 제공하지 않고 접근하려 할 때, 401 UnAuthorized 에러를 리턴
 * -> Spring Security가 AuthenticationEntryPoint 인터페이스의 commence 메서드 호출
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

//    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.info("인증되지 않은 요청입니다.");
        ErrorResponse errorResponse = ErrorResponse.of(NOT_AUTHENTICATED_REQUEST.isSuccess(), NOT_AUTHENTICATED_REQUEST.getCode(), NOT_AUTHENTICATED_REQUEST.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
    }
}