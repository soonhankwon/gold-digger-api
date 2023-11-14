package dev.golddiggerapi.exception.detail;

import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Set;

public record ConstraintViolationError(String propertyPath, Object rejectedValue, String reason) {

    public static List<ConstraintViolationError> of(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(constraintViolation -> new ConstraintViolationError(
                        constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getInvalidValue().toString(),
                        constraintViolation.getMessage()
                )).toList();
    }
}
