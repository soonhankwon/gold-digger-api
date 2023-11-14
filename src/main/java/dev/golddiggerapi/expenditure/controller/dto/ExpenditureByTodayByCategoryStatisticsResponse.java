package dev.golddiggerapi.expenditure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지출 카테고리별 통계 응답 DTO")
public record ExpenditureByTodayByCategoryStatisticsResponse(

        @Schema(description = "카테고리 ID", example = "1")
        Long categoryId,

        @Schema(description = "카테고리 이름", example = "식비")
        String name,

        @Schema(description = "카테고리 지출합계", example = "20000")
        Long expenditureSum,

        @Schema(description = "오늘 기준 적절 지출액", example = "10000")
        Long reasonableExpenditureSum,

        @Schema(description = "위험도", example = "200%")
        String risk
) {

    public static ExpenditureByTodayByCategoryStatisticsResponse toResponse(ExpenditureCategoryAndAmountResponse response, ExpenditureAnalyze analyze) {
        return new ExpenditureByTodayByCategoryStatisticsResponse(
                response.categoryId(),
                response.name(),
                response.sum(),
                analyze.reasonableExpenditureSum(),
                String.valueOf(analyze.risk()) + '%'
        );
    }
}
