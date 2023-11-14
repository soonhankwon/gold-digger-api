package dev.golddiggerapi.expenditure.controller;

import dev.golddiggerapi.expenditure.controller.dto.*;
import dev.golddiggerapi.expenditure.service.ExpenditureService;
import dev.golddiggerapi.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    @PostMapping("/{categoryId}")
    public ResponseEntity<String> createExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @PathVariable Long categoryId,
                                                    @Validated @RequestBody ExpenditureRequest request) {
        String res = expenditureService.createExpenditure(userPrincipal.getUsername(), categoryId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping("/{expenditureId}")
    public ResponseEntity<String> updateExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @PathVariable Long expenditureId,
                                                    @Validated @RequestBody ExpenditureUpdateRequest request) {
        String res = expenditureService.updateExpenditure(userPrincipal.getUsername(), expenditureId, request);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{expenditureId}")
    public ResponseEntity<ExpenditureResponse> getExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              @PathVariable Long expenditureId) {
        ExpenditureResponse res = expenditureService.getExpenditure(userPrincipal.getUsername(), expenditureId);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    public ResponseEntity<ExpenditureByUserResponse> getExpendituresByUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                           @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                                           @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                                                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                                           @RequestParam(value = "minAndMax", required = false) Boolean hasMinAndMax) {
        ExpenditureByUserRequest request = new ExpenditureByUserRequest(start, end, categoryId, hasMinAndMax);
        ExpenditureByUserResponse res = expenditureService.getExpendituresByUser(userPrincipal.getUsername(), request);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{expenditureId}")
    public ResponseEntity<String> deleteExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @PathVariable Long expenditureId) {
        String res = expenditureService.deleteExpenditure(userPrincipal.getUsername(), expenditureId);
        return ResponseEntity.ok().body(res);
    }

    @PatchMapping("/{expenditureId}/exclude")
    public ResponseEntity<String> excludeExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @PathVariable Long expenditureId) {
        String res = expenditureService.excludeExpenditure(userPrincipal.getUsername(), expenditureId);
        return ResponseEntity.ok().body(res);
    }

    // 오늘 지출 추천
    @GetMapping("/today/recommend")
    public ResponseEntity<ExpenditureByTodayRecommendationResponse> getExpenditureRecommendationByToday(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        ExpenditureByTodayRecommendationResponse res = expenditureService.getExpenditureRecommendationByToday(userPrincipal.getUsername());
        return ResponseEntity.ok().body(res);
    }

    // 오늘 지출 분석 및 안내
    @GetMapping("/today")
    public ResponseEntity<ExpenditureByTodayResponse> getExpenditureByToday(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        ExpenditureByTodayResponse res = expenditureService.getExpenditureByToday(userPrincipal.getUsername());
        return ResponseEntity.ok().body(res);
    }

    // 유저 카테고리별 지출 평균 비율 (유저 지출 비율 기준으로 통계)
    @GetMapping("/avg-ratio")
    public ResponseEntity<List<UserExpenditureAvgRatioByCategoryStatisticResponse>> statisticExpenditureAvgRatioByCategory() {
        List<UserExpenditureAvgRatioByCategoryStatisticResponse> res = expenditureService.statisticExpenditureAvgRatioByCategory();
        return ResponseEntity.ok().body(res);
    }
}
