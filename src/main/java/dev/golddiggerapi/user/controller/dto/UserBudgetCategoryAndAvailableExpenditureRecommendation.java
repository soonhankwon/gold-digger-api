package dev.golddiggerapi.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 예산 카테고리별 지출가능 금액 추천 DTO")
public record UserBudgetCategoryAndAvailableExpenditureRecommendation(

        @Schema(description = "카테고리 ID", example = "1")
        Long categoryId,

        @Schema(description = "카테고리 이름", example = "식비")
        String name,

        @Schema(description = "지출가능 금액", example = "20000")
        Long availableExpenditure
) {
    public UserBudgetCategoryAndAvailableExpenditureRecommendation(Long categoryId, String name, Long availableExpenditure) {
        this.categoryId = categoryId;
        this.name = name;
        if (availableExpenditure < 0) {
            throw new IllegalArgumentException("minimum expenditure is 0");
        }
        this.availableExpenditure = Math.round(availableExpenditure / 100.0) * 100;
    }

    public static UserBudgetCategoryAndAvailableExpenditureRecommendation toRecommendation(UserBudgetCategoryAndAvailableExpenditure userBudgetCategoryAndAvailableExpenditure) {
        return new UserBudgetCategoryAndAvailableExpenditureRecommendation(
                userBudgetCategoryAndAvailableExpenditure.categoryId(),
                userBudgetCategoryAndAvailableExpenditure.name(),
                userBudgetCategoryAndAvailableExpenditure.availableExpenditure()
        );
    }

    public static UserBudgetCategoryAndAvailableExpenditureRecommendation toMinimumRecommendation(UserBudgetCategoryAndAvailableExpenditure userBudgetCategoryAndAvailableExpenditure, Long availableExpenditure) {
        return new UserBudgetCategoryAndAvailableExpenditureRecommendation(
                userBudgetCategoryAndAvailableExpenditure.categoryId(),
                userBudgetCategoryAndAvailableExpenditure.name(),
                availableExpenditure
        );
    }
}
