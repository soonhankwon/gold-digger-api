package dev.golddiggerapi.user.controller.dto;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;

public record UserBudgetRecommendation(
        ExpenditureCategory category,
        Long amount
) {
}
