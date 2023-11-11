package dev.golddiggerapi.user.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;

public record UserBudgetAvgRatioByCategoryStatisticResponse(
        ExpenditureCategory category,
        Double avgRatio
) {

    @QueryProjection
    public UserBudgetAvgRatioByCategoryStatisticResponse {
    }
}
