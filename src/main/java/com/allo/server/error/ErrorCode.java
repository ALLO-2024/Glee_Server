package com.allo.server.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_EMAIL("A1", "이미 등록된 이메일입니다."),
    ALREADY_EXIST_NICKNAME("A2", "이미 사용 중인 닉네임입니다."),
    ALREADY_LOGOUT_MEMBER("A3", "이미 로그아웃한 회원입니다"),
    EMAIL_SEND_ERROR("A4", "이메일 인증 코드 전송을 실패했습니다."),
    UNKNOWN_PROVIDER("A5", "provider 값이 KAKAO 또는 NAVER가 아닙니다."),
    NOT_ALLOWED_MEMBER("A6", "해당 요청에 대한 권한이 없습니다."),
    NOT_AUTHENTICATED_REQUEST("A7", "유효한 JWT 토큰이 없습니다."),


    USER_NOT_FOUND("M1", "사용자를 찾을 수 없습니다."), 
    INVALID_ROLE_NAME("M3", "유효한 ROLE_NAME 이 없습니다."),
    NOT_FOUND_ROLE_NAME("M4", "해당 토큰에서 ROLE_NAME을 찾을 수 없습니다."),

    TOKEN_NOT_EXIST("T1", "토큰이 존재하지 않습니다."),
    TOKEN_IS_EXPIRED("T2", "만료된 토큰입니다."),
    INVALID_TOKEN("T3", "잘못된 토큰입니다."),
    TOKEN_NOT_CREATED("T4", "토큰 생성에 실패했습니다."),
    TOKEN_NOT_MATCHED("T5", "해당 RefreshToken을 Redis에서 찾을 수 없습니다.");

    private final String code;
    private final String message;

}
