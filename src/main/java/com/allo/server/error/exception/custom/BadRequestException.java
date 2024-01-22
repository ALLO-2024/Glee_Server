package com.allo.server.error.exception.custom;

import com.allo.server.error.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final String code;

    public BadRequestException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

}
