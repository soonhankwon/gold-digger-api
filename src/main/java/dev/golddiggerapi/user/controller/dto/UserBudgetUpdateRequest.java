package dev.golddiggerapi.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "유저예산 수정 요청 DTO")
public record UserBudgetUpdateRequest(

        @Schema(description = "수정 예산액", example = "400000")
        @Min(value = 0, message = "예산은 0보다 작을수 없습니다.")
        @Max(value = 10_000_000_000L, message = "예산은 100억 보다 클수 없습니다.")
        Long amount,

        @Schema(description = "수정년도", example = "2023")
        @Min(value = 2022, message = "2022년부터 예산을 설정할 수 있습니다.")
        @Max(value = 2100, message = "최대 2100년까지 예산을 설정할 수 있습니다.")
        Integer year,

        @Schema(description = "수정월", example = "12")
        @Min(1) @Max(12)
        Integer month,

        @Schema(description = "수정 카테고리 ID", example = "2")
        @NotNull
        Long categoryId
) {
    public UserBudgetUpdateRequest(@Min(value = 0) Long amount,
                                   Integer year,
                                   @Min(1) @Max(12) Integer month,
                                   @NotNull Long categoryId) {
        this.amount = amount;
        this.year = year;
        this.month = month;
        this.categoryId = categoryId;
    }
}
