package dev.golddiggerapi.expenditure.controller.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ExpenditureCategoryAndAmountResponse(
        Long categoryId,
        String name,
        Long sum
) {

    @QueryProjection
    public ExpenditureCategoryAndAmountResponse {
    }
}
