package com.allo.server.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_EMAIL(false,2000, "이미 등록된 이메일입니다."),
    ALREADY_EXIST_NICKNAME(false,2001, "이미 사용 중인 닉네임입니다."),
    ALREADY_LOGOUT_MEMBER(false,2002, "이미 로그아웃한 회원입니다"),
    EMAIL_SEND_ERROR(false,2003, "이메일 인증 코드 전송을 실패했습니다."),
    UNKNOWN_PROVIDER(false,2004, "provider 값이 KAKAO 또는 NAVER가 아닙니다."),
    NOT_ALLOWED_MEMBER(false,2005, "해당 요청에 대한 권한이 없습니다."),
    NOT_AUTHENTICATED_REQUEST(false,2006, "유효한 JWT 토큰이 없습니다."),
    FILE_NOT_FOUND(false,2007, "파일이 존재하지 않습니다."),

    USER_NOT_FOUND(false,2008, "사용자를 찾을 수 없습니다."),
    INVALID_ROLE_NAME(false,2009, "유효한 ROLE_NAME 이 없습니다."),
    NOT_FOUND_ROLE_NAME(false,2010, "해당 토큰에서 ROLE_NAME을 찾을 수 없습니다."),

    TOKEN_NOT_EXIST(false,2011, "토큰이 존재하지 않습니다."),
    TOKEN_IS_EXPIRED(false,2012, "만료된 토큰입니다."),
    INVALID_TOKEN(false,2013, "잘못된 토큰입니다."),
    TOKEN_NOT_CREATED(false,2014, "토큰 생성에 실패했습니다."),
    TOKEN_NOT_MATCHED(false,2015, "해당 RefreshToken을 Redis에서 찾을 수 없습니다."),

    LECTURE_NOT_FOUND(false,3000, "lectureId에 해당하는 강의를 찾을 수 없습니다."),
    UNKNOWN_LECTURE_LANGUAGE(false,3001, "lectureLanguage 값이 ENGLISH, CHINESE, JAPANESE와 VIETNAMESE가 아닙니다."),
    UNKNOWN_LECTURE_TYPE(false,3002, "lectureType 값이 MAJOR, CULTURE_ESSENTIAL와 CULTURE_SELECT가 아닙니다."),
    UNKNOWN_LECTURE_SUBJECT(false,3003, "lectureSubject 값이 HUMANITY, SOCIAL, EDUCATION, SCIENCE, ENTERTAINMENT와 CULTURE가 아닙니다."),

    UNKNOWN_WORD(false,4000, "word 값이 한국어 기초사전에 존재하지 않는 단어 입니다."),
    ALREADY_EXIST_WORD(false,4001, "이미 존재하는 단어 입니다.")


    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

}
