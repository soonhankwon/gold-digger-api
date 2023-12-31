package dev.golddiggerapi.exception.detail;

import org.springframework.validation.BindingResult;

import java.util.List;

public record FieldError(String field, Object rejectedValue, String reason) {

    public static List<FieldError> of(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                        error.getDefaultMessage()
                )).toList();
    }
}
