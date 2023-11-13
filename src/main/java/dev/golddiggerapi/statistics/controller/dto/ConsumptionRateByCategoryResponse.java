package dev.golddiggerapi.statistics.controller.dto;

public record ConsumptionRateByCategoryResponse(
        Long categoryId,
        String name,
        String consumptionRate
) {
    public static ConsumptionRateByCategoryResponse toResponse(ConsumptionRateByCategoryStatistics consumptionRateByCategoryStatistics) {
        return new ConsumptionRateByCategoryResponse(
                consumptionRateByCategoryStatistics.categoryId(),
                consumptionRateByCategoryStatistics.name(),
                String.valueOf(consumptionRateByCategoryStatistics.consumptionRate()) + '%'
        );
    }
}
