package dev.golddiggerapi.expenditure.controller;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureCategoryResponse;
import dev.golddiggerapi.expenditure.service.ExpenditureCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures/categories")
@Tag(name = "지출 카테고리 API")
public class ExpenditureCategoryController {

    private final ExpenditureCategoryService expenditureCategoryService;

    @Operation(summary = "지출 카테고리 목록 조회 API")
    @GetMapping
    public ResponseEntity<List<ExpenditureCategoryResponse>> getExpenditureCategories() {
        List<ExpenditureCategoryResponse> res =
                expenditureCategoryService.getExpenditureCategories();
        return ResponseEntity.ok().body(res);
    }
}
