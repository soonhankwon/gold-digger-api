package dev.golddiggerapi.expenditure.controller.dto;

import com.querydsl.core.annotations.QueryProjection;

public record UserExpenditureAvgRatioByCategoryStatisticResponse(
        Long categoryId,
        String name,
        Double avgRatio
) {
    @QueryProjection
    public UserExpenditureAvgRatioByCategoryStatisticResponse {
    }
}
