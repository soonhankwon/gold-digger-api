package dev.golddiggerapi.exception;

import dev.golddiggerapi.exception.detail.ApiException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> apiExHandle(ApiException e) {
        HttpStatus status = e.getCustomErrorCode().getStatus();
        ErrorResponse response = ErrorResponse.of(e.getCustomErrorCode());
        return new ResponseEntity<>(response, status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse constraintViolationExHandle(ConstraintViolationException e) {
        return ErrorResponse.of(e.getConstraintViolations());
    }

}
