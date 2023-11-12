package dev.golddiggerapi.user.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Year;

public record UserBudgetCreateRequest(
        @Min(value = 0)
        Long amount,

        Integer year,

        @Min(1) @Max(12)
        Integer month
) {
    public UserBudgetCreateRequest(@Min(value = 0) Long amount,
                                   Integer year,
                                   @Min(1) @Max(12) Integer month) {
        this.amount = amount;
        if (year < Year.now().getValue()) {
            throw new IllegalArgumentException("year can't before now year");
        }
        this.year = year;
        this.month = month;
    }
}
