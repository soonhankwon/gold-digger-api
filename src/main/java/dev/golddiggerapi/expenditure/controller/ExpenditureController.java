package dev.golddiggerapi.expenditure.controller;

import dev.golddiggerapi.expenditure.controller.dto.*;
import dev.golddiggerapi.expenditure.service.ExpenditureService;
import dev.golddiggerapi.expenditure.service.ExpenditureServiceHandler;
import dev.golddiggerapi.security.principal.UserPrincipal;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures")
@Tag(name = "지출 API")
public class ExpenditureController {

    private final ExpenditureService expenditureService;
    private final ExpenditureServiceHandler expenditureServiceHandler;

    @Operation(summary = "지출 생성 API", responses = @ApiResponse(responseCode = "201"))
    @PostMapping("/{categoryId}")
    public ResponseEntity<String> createExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @Parameter(description = "지출의 카테고리 ID", required = true)
                                                    @PathVariable Long categoryId,
                                                    @Validated @RequestBody ExpenditureRequest request) {
        String res = expenditureService.createExpenditure(userPrincipal.getUsername(), categoryId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary = "지출 수정 API")
    @PatchMapping("/{expenditureId}")
    public ResponseEntity<String> updateExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @PathVariable Long expenditureId,
                                                    @Validated @RequestBody ExpenditureUpdateRequest request) {
        String res = expenditureService.updateExpenditure(userPrincipal.getUsername(), expenditureId, request);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "지출 상세조회 API")
    @GetMapping("/{expenditureId}")
    public ResponseEntity<ExpenditureResponse> getExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              @PathVariable Long expenditureId) {
        ExpenditureResponse res = expenditureService.getExpenditure(userPrincipal.getUsername(), expenditureId);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "지출 목록조회 API", description = "필수적으로 기간으로 조회, 모든 내용의 지출 합계, 카테고리별 지출 합계 반환 [특정 카테고리 ID 포함시 해당 카테고리로만 조회]")
    @GetMapping
    public ResponseEntity<ExpenditureByUserResponse> getExpendituresByUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                           @Parameter(description = "시작일", required = true)
                                                                           @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                                           @Parameter(description = "종료일", required = true)
                                                                           @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                                                           @Parameter(description = "특정 카테고리 ID")
                                                                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                                           @Parameter(description = "최소, 최대지출 포함여부")
                                                                           @RequestParam(value = "minAndMax", required = false) Boolean hasMinAndMax) {
        ExpenditureByUserRequest request = new ExpenditureByUserRequest(start, end, categoryId, hasMinAndMax);
        ExpenditureByUserResponse res = expenditureService.getExpendituresByUser(userPrincipal.getUsername(), request);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "지출 삭제 API")
    @DeleteMapping("/{expenditureId}")
    public ResponseEntity<String> deleteExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @PathVariable Long expenditureId) {
        String res = expenditureService.deleteExpenditure(userPrincipal.getUsername(), expenditureId);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "지출 합계 제외 API")
    @PatchMapping("/{expenditureId}/exclude")
    public ResponseEntity<String> excludeExpenditure(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @PathVariable Long expenditureId) {
        String res = expenditureService.excludeExpenditure(userPrincipal.getUsername(), expenditureId);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "오늘 지출 추천 API v1")
    @GetMapping("/v1/today/recommend")
    public ResponseEntity<ExpenditureByTodayRecommendationResponse> getExpenditureRecommendationByTodayV1(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        ExpenditureByTodayRecommendationResponse res = expenditureServiceHandler.getExpenditureRecommendationByToday(userPrincipal.getUsername());
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "오늘 지출 분석 및 안내 API")
    @GetMapping("/today")
    public ResponseEntity<ExpenditureByTodayResponse> getExpenditureByToday(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        ExpenditureByTodayResponse res = expenditureService.getExpenditureByToday(userPrincipal.getUsername());
        return ResponseEntity.ok().body(res);
    }

    @Hidden
    @GetMapping("/test/web-hook")
    public ResponseEntity<String> testWebHook() {
//        expenditureService.sendExpenditureRecommendationByToday();
        expenditureServiceHandler.sendExpenditureRecommendationByToday();
        expenditureService.sendExpenditureByToday();
        return ResponseEntity.ok().body("send webhook");
    }
}
