package com.allo.server.error.exception.custom;

import com.allo.server.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {

    private final String code;

    public TokenException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }


}
