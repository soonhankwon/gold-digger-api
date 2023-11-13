package dev.golddiggerapi.statistics.controller.dto;

import java.util.List;

public record ExpenditureStatisticsResponse(
        String consumptionRateCompareToPreviousMonth,
        String consumptionRateCompareToPreviousDay,
        String consumptionRateCompareToOtherUsersConsumptionRate,
        List<ConsumptionRateByCategoryResponse> consumptionRateByCategoryStatistics
) {
}
