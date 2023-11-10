package dev.golddiggerapi.expenditure.controller.dto;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;

public record ExpenditureCategoryResponse(
        Long id,
        String name
) {
    public static ExpenditureCategoryResponse toResponse(ExpenditureCategory category) {
        return new ExpenditureCategoryResponse(
                category.getId(),
                category.getName());
    }
}
