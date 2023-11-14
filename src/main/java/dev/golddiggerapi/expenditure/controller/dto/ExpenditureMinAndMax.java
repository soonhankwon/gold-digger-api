package dev.golddiggerapi.expenditure.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지출 최소, 최대금액 DTO")
public record ExpenditureMinAndMax(

        @Schema(description = "최소값", example = "10000")
        Long min,

        @Schema(description = "최대값", example = "40000")
        Long max
) {
    @QueryProjection
    public ExpenditureMinAndMax {
    }
}
