package dev.golddiggerapi.user.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserBudgetUpdateRequest(
        @Min(value = 0)
        Long amount,

        @NotBlank
        Long categoryId
) {
}
