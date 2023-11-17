package dev.golddiggerapi.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "유저예산 수정 요청 DTO")
public record UserBudgetUpdateRequest(

        @Schema(description = "수정 예산액", example = "400000")
        @Min(value = 0)
        Long amount,

        @Schema(description = "수정년도", example = "2023")
        @Max(value = 2100)
        Integer year,

        @Schema(description = "수정월", example = "12")
        @Min(1) @Max(12)
        Integer month,

        @Schema(description = "수정 카테고리 ID", example = "2")
        @NotBlank
        Long categoryId
) {
    public UserBudgetUpdateRequest(@Min(value = 0) Long amount,
                                   Integer year,
                                   @Min(1) @Max(12) Integer month,
                                   @NotBlank Long categoryId) {
        this.amount = amount;
        this.year = year;
        this.month = month;
        this.categoryId = categoryId;
    }
}
