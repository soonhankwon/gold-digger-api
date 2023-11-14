package dev.golddiggerapi.expenditure.controller.dto;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "지출 카테고리 응답 DTO")
public record ExpenditureCategoryResponse(

        @Schema(description = "카테고리 ID", example = "1")
        Long id,

        @Schema(description = "카테고리 이름", example = "식비")
        String name
) {
    public static ExpenditureCategoryResponse toResponse(ExpenditureCategory category) {
        return new ExpenditureCategoryResponse(
                category.getId(),
                category.getName());
    }
}
