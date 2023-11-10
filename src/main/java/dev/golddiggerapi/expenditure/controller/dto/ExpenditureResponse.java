package dev.golddiggerapi.expenditure.controller.dto;

import dev.golddiggerapi.expenditure.domain.Expenditure;
import dev.golddiggerapi.expenditure.domain.ExpenditureStatus;

import java.time.LocalDateTime;

public record ExpenditureResponse(
        Long id,
        Long amount,
        String memo,
        LocalDateTime expenditureDateTime,
        ExpenditureStatus expenditureStatus,
        LocalDateTime createdAt,
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
