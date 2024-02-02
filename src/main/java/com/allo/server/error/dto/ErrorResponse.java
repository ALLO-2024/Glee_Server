package com.allo.server.error.dto;

public record ErrorResponse(Boolean isSuccess, int code, String message) {
    public static ErrorResponse of(Boolean isSuccess, int code, String message){
        return new ErrorResponse(isSuccess, code, message);
    }

}