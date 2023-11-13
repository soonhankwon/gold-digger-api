package dev.golddiggerapi.statistics.controller.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ConsumptionRateByCategoryStatistics(
        Long categoryId,
        String name,
        Long consumptionRate
) {
    @QueryProjection
    public ConsumptionRateByCategoryStatistics {
    }
}
