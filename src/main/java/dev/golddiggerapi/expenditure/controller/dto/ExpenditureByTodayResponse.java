package dev.golddiggerapi.expenditure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "오늘 지출 안내 및 분석 응답 DTO")
public record ExpenditureByTodayResponse(

        @Schema(description = "오늘 지출 총액", example = "47200")
        Long expenditureSum,

        @Schema(description = "오늘 기준 사용 적절한 지출 금액", example = "42000")
        Long reasonableExpenditureSum,

        List<ExpenditureByTodayByCategoryStatisticsResponse> expenditureByTodayByCategoryStatisticsResponses
) {
}
