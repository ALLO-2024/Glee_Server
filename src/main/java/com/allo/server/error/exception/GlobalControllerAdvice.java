package com.allo.server.error.exception;

import com.allo.server.error.dto.ErrorResponse;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.error.exception.custom.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<String> handle(final Exception e) {
        log.error("Internal Error occurred", e);
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(final BadRequestException e) {
        log.info("Bad Request: {}", e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(final TokenException e) {
        log.info("Invalid Token: {}", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        // 에러 반환이 여러 개일 경우 첫 번째 에러를 반환
        String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        // 모든 에러 메시지는 로그로 남김
        List<String> errorList = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
        log.warn("MethodArgumentNotValidExceptionException = {}", errorList);

        return ResponseEntity.status(BAD_REQUEST).body(ErrorResponse.of("V1", firstErrorMessage));
    }


}
