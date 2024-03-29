package com.allo.server.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    NOT_ALLOW_METHOD(false, 405, "잘못된 메소드입니다."),
    INTERNAL_SERVER_ERROR(false, 500, "서버에러 입니다."),
    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 2003, "권한이 없는 유저의 접근입니다."),
    INVALID_USER(false, 2004, "존재하지 않는 유저입니다."),
    INVALID_IMAGE_FILE(false, 2005, "이미지파일이 아닙니다."),

    FAILED_TO_CREATE_TOKEN(false, 2006, "토큰 생성에 실패했습니다."),
    INVALID_TOKEN(false, 2007, "유효하지 않은 TOKEN 입니다."),
    TOKEN_NOT_MATCHED(false, 2008, "일치하지 않는 TOKEN 입니다."),
    INVALID_ROLE_NAME(false, 2009, "유효하지 않은 ROLE_NAME 입니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false, 3014, "없는 아이디거나 비밀번호가 틀렸습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}