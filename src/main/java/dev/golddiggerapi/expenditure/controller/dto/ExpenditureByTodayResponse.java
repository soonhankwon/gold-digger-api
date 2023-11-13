package dev.golddiggerapi.expenditure.controller.dto;

import java.util.List;

public record ExpenditureByTodayResponse(
        Long expenditureSum,
        Long reasonableExpenditureSum,
        List<ExpenditureByTodayByCategoryStatisticsResponse> expenditureByTodayByCategoryStatisticsResponses
) {
}
