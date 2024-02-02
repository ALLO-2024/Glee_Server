package com.allo.server.error.exception.custom;

import com.allo.server.error.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final Boolean isSuccess;
    private final int code;
    private final String message;

    public BadRequestException(final ErrorCode errorCode) {
        this.isSuccess = errorCode.isSuccess();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
