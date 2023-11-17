package dev.golddiggerapi.exception;

import dev.golddiggerapi.exception.detail.ConstraintViolationError;
import dev.golddiggerapi.exception.detail.FieldError;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Set;

@Getter
public class ErrorResponse {

    private final HttpStatus status;

    private final String message;

    private List<FieldError> fieldErrors;

    private List<ConstraintViolationError> violationErrors;

    private ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    private ErrorResponse(List<FieldError> fieldErrors, List<ConstraintViolationError> violationErrors) {
        this.status = HttpStatus.BAD_REQUEST;
        this.message = HttpStatus.BAD_REQUEST.getReasonPhrase();
        this.fieldErrors = fieldErrors;
        this.violationErrors = violationErrors;
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        return new ErrorResponse(null, ConstraintViolationError.of(violations));
    }

    public static ErrorResponse of(CustomErrorCode customErrorCode) {
        return new ErrorResponse(customErrorCode.getStatus(), customErrorCode.getMessage());
    }
}