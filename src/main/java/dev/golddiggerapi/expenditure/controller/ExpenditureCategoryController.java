package dev.golddiggerapi.expenditure.controller;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureCategoryResponse;
import dev.golddiggerapi.expenditure.service.ExpenditureCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenditures/categories")
@RequiredArgsConstructor
public class ExpenditureCategoryController {

    private final ExpenditureCategoryService expenditureCategoryService;

    @GetMapping
    public ResponseEntity<List<ExpenditureCategoryResponse>> getExpenditureCategories() {
        List<ExpenditureCategoryResponse> res =
                expenditureCategoryService.getExpenditureCategories();
        return ResponseEntity.ok().body(res);
    }
}
