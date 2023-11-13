package dev.golddiggerapi.user.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.Year;

public record UserBudgetUpdateRequest(
        @Min(value = 0)
        Long amount,

        @Max(value = 2100)
        Integer year,

        @Min(1) @Max(12)
        Integer month,

        @NotBlank
        Long categoryId
) {
        public UserBudgetUpdateRequest(@Min(value = 0) Long amount,
                                       Integer year,
                                       @Min(1) @Max(12) Integer month,
                                       @NotBlank Long categoryId) {
                this.amount = amount;
                if (year < Year.now().getValue()) {
                        throw new IllegalArgumentException("year can't before now year");
                }
                this.year = year;
                this.month = month;
                this.categoryId = categoryId;
        }
}
