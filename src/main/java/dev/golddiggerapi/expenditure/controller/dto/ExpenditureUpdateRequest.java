package dev.golddiggerapi.expenditure.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ExpenditureUpdateRequest(
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:00")
        @NotBlank
        String dateTime,
        @Min(value = 0)
        Long amount,
        @NotNull
        String memo,
        @NotNull
        Long categoryId
) {
}
