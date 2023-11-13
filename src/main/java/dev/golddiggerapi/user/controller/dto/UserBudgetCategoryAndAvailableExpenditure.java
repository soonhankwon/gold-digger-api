package dev.golddiggerapi.user.controller.dto;

import com.querydsl.core.annotations.QueryProjection;

public record UserBudgetCategoryAndAvailableExpenditure(
        Long categoryId,
        String name,
        Long availableExpenditure
) {

    @QueryProjection
    public UserBudgetCategoryAndAvailableExpenditure {
    }
}
