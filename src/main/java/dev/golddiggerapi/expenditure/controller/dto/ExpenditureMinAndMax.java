package dev.golddiggerapi.expenditure.controller.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ExpenditureMinAndMax(
        Long min,
        Long max
) {
    @QueryProjection
    public ExpenditureMinAndMax {
    }
}
