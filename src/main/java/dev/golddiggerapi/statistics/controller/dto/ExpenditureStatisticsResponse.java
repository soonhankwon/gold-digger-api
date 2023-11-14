package dev.golddiggerapi.statistics.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "지출 통계 응답 DTO")
public record ExpenditureStatisticsResponse(

        @Schema(description = "지날 달 대비 총액 소비율", example = "150%")
        String consumptionRateCompareToPreviousMonth,

        @Schema(description = "지날 요일 대비 총액 소비율", example = "80%")
        String consumptionRateCompareToPreviousDay,

        @Schema(description = "다른 유저 대비 소비율", example = "120%")
        String consumptionRateCompareToOtherUsersConsumptionRate,

        List<ConsumptionRateByCategoryResponse> consumptionRateByCategoryStatistics
) {
}
