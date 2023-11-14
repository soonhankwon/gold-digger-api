package dev.golddiggerapi.expenditure.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지출 내역 메모 & 금액 DTO")
public record ExpenditureDetailsResponse(

        @Schema(description = "카테고리 ID", example = "1")
        Long categoryId,

        @Schema(description = "메모", example = "스테이크")
        String memo,

        @Schema(description = "금액", example = "35000")
        Long amount
) {
    @QueryProjection
    public ExpenditureDetailsResponse {
    }
}
