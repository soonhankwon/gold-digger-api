package dev.golddiggerapi.expenditure.controller.dto;

public record ExpenditureByTodayByCategoryStatisticsResponse(
        Long categoryId,
        String name,
        Long expenditureSum,
        Long reasonableExpenditureSum,
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
