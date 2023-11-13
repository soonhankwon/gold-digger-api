package dev.golddiggerapi.user.controller.dto;

public record UserBudgetCategoryAndAvailableExpenditureRecommendation(
        Long categoryId,
        String name,
        Long availableExpenditure
) {

    public UserBudgetCategoryAndAvailableExpenditureRecommendation(Long categoryId, String name, Long availableExpenditure) {
        this.categoryId = categoryId;
        this.name = name;
        if(availableExpenditure < 0) {
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
