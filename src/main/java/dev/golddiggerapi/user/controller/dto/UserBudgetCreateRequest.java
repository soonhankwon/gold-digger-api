package dev.golddiggerapi.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Year;

@Schema(description = "유저예산 설정 요청 DTO")
public record UserBudgetCreateRequest(

        @Schema(description = "예산액", example = "500000")
        @Min(value = 0)
        Long amount,

        @Schema(description = "설정년도", example = "2023")
        @Max(value = 2100)
        Integer year,

        @Schema(description = "설정달", example = "11")
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
