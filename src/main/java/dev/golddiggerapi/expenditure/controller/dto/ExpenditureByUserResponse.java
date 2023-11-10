package dev.golddiggerapi.expenditure.controller.dto;

import java.util.List;

public record ExpenditureByUserResponse(
        Long expenditureSum,
        ExpenditureMinAndMax expenditureMinAndMax,
        List<ExpenditureCategoryAndAmountResponse> expenditureCategoryAndAmountResponses,
        List<ExpenditureMemoAndAmountResponse> expenditureMemoAndAmountResponses
) {
}
