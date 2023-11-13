package dev.golddiggerapi.expenditure.controller.dto;

import dev.golddiggerapi.user.controller.dto.UserBudgetCategoryAndAvailableExpenditureRecommendation;

import java.util.List;

public record ExpenditureByTodayRecommendationResponse(
        Long availableTotalExpenditure,
        String message,
        List<UserBudgetCategoryAndAvailableExpenditureRecommendation> userBudgetCategoryAndAmountStatistics
) {
}
