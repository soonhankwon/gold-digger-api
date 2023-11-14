package dev.golddiggerapi.expenditure.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지출 카테고리별 지출금액 응답 DTO")
public record ExpenditureCategoryAndAmountResponse(

        @Schema(description = "카테고리 ID", example = "1")
        Long categoryId,

        @Schema(description = "카테고리 이름", example = "식비")
        String name,

        @Schema(description = "카테고리 지출 총합", example = "50000")
        Long sum
) {

    @QueryProjection
    public ExpenditureCategoryAndAmountResponse {
    }
}
