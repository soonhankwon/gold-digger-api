package dev.golddiggerapi.expenditure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "유저 지출 목록 조회 응답 DTO")
public record ExpenditureByUserResponse(

        @Schema(description = "지출 합계", example = "130000")
        Long expenditureSum,

        ExpenditureMinAndMax expenditureMinAndMax,

        List<ExpenditureCategoryAndAmountResponse> expenditureCategoryAndAmountResponses,

        List<ExpenditureDetailsResponse> expenditureDetailsResponse
) {
}
