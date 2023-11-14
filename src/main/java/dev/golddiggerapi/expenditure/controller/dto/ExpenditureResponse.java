package dev.golddiggerapi.expenditure.controller.dto;

import dev.golddiggerapi.expenditure.domain.Expenditure;
import dev.golddiggerapi.expenditure.domain.ExpenditureStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "지출 상세조회 응답 DTO")
public record ExpenditureResponse(

        @Schema(description = "지출 ID", example = "1")
        Long id,

        @Schema(description = "지출액", example = "20000")
        Long amount,

        @Schema(description = "메모", example = "저녁값")
        String memo,

        @Schema(description = "지출일시", example = "2023-11-14T13:00:00")
        LocalDateTime expenditureDateTime,

        @Schema(description = "지출상태", example = "INCLUDED")
        ExpenditureStatus expenditureStatus,

        @Schema(description = "생성일시", example = "2023-11-14T19:50:39.234293")
        LocalDateTime createdAt,

        @Schema(description = "수정일시", example = "2023-11-14T19:50:39.234325")
        LocalDateTime updatedAt
) {

    public static ExpenditureResponse toResponse(Expenditure expenditure) {
        return new ExpenditureResponse(
                expenditure.getId(),
                expenditure.getAmount(),
                expenditure.getMemo(),
                expenditure.getExpenditureDateTime(),
                expenditure.getExpenditureStatus(),
                expenditure.getCreatedAt(),
                expenditure.getUpdatedAt());
    }
}
