package dev.golddiggerapi.user.controller.dto;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저예산 추천 DTO")
public record UserBudgetRecommendation(

        ExpenditureCategory category,

        @Schema(description = "카테고리별 추천예산", example = "350000")
        Long amount
) {
}
