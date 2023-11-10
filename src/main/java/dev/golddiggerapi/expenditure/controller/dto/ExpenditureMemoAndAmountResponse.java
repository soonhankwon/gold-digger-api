package dev.golddiggerapi.expenditure.controller.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ExpenditureMemoAndAmountResponse(
        Long categoryId,
        String memo,
        Long amount
) {
    @QueryProjection
    public ExpenditureMemoAndAmountResponse {
    }
}
