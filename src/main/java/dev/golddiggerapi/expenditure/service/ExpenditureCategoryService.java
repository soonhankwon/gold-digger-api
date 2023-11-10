package dev.golddiggerapi.expenditure.service;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureCategoryResponse;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenditureCategoryService {

    private final ExpenditureCategoryRepository expenditureCategoryRepository;

    public List<ExpenditureCategoryResponse> getExpenditureCategories() {
        List<ExpenditureCategory> categories = expenditureCategoryRepository.findAll();

        return categories.stream()
                .map(ExpenditureCategoryResponse::toResponse)
                .collect(Collectors.toList());
    }
}
