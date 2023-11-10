package dev.golddiggerapi.user.controller.dto;

import jakarta.validation.constraints.Min;

public record UserBudgetCreateRequest(
        @Min(value = 0)
        Long amount
) {
}
