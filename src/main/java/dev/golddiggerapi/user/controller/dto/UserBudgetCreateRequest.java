package dev.golddiggerapi.user.controller.dto;

import dev.golddiggerapi.exception.CustomErrorCode;
import dev.golddiggerapi.exception.detail.ApiException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Year;

@Schema(description = "유저예산 설정 요청 DTO")
public record UserBudgetCreateRequest(

        @Schema(description = "예산액", example = "500000")
        @Min(value = 0, message = "예산은 0 보다 작을수 없습니다.")
        @Max(value = 10_000_000_000L, message = "예산은 100억 보다 클수 없습니다.")
        Long amount,

        @Schema(description = "설정년도", example = "2023")
        @Max(2100)
        Integer year,

        @Schema(description = "설정달", example = "11")
        @Min(1) @Max(12)
        Integer month
) {
    public UserBudgetCreateRequest(@Min(value = 0) Long amount,
                                   Integer year,
                                   @Min(1) @Max(12) Integer month) {
        this.amount = amount;
        if (isYearBeforeNowYear(year)) {
            throw new ApiException(CustomErrorCode.INVALID_PARAMETER_YEAR_BEFORE_NOW);
        }
        this.year = year;
        this.month = month;
    }

    private boolean isYearBeforeNowYear(Integer year) {
        return year < Year.now().getValue();
    }
}
