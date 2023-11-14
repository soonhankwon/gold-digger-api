package dev.golddiggerapi.statistics.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리별 소비율 응답 DTO")
public record ConsumptionRateByCategoryResponse(

        @Schema(description = "카테고리 ID", example = "1")
        Long categoryId,

        @Schema(description = "카테고리 이름", example = "식비")
        String name,

        @Schema(description = "소비율", example = "60%")
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
