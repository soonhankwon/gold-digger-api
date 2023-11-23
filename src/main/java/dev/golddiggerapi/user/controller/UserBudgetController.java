package dev.golddiggerapi.user.controller;

import dev.golddiggerapi.security.principal.UserPrincipal;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetRecommendation;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import dev.golddiggerapi.user.service.UserBudgetService;
import dev.golddiggerapi.user.service.UserBudgetServiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budget")
@Tag(name = "유저예산 API")
public class UserBudgetController {

    private final UserBudgetService userBudgetService;
    private final UserBudgetServiceHandler userBudgetServiceHandler;

    @Operation(summary = "유저예산 설정 API")
    @PostMapping
    public ResponseEntity<String> createUserBudget(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @Parameter(description = "설정예산의 카테고리 ID", required = true)
                                                   @RequestParam(value = "categoryId") Long categoryId,
                                                   @Validated @RequestBody UserBudgetCreateRequest request) {
        String res = userBudgetServiceHandler.createUserBudget(userPrincipal.getUsername(), categoryId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary = "유저예산 수정 API")
    @PatchMapping("/{userBudgetId}")
    public ResponseEntity<String> updateUserBudget(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @PathVariable Long userBudgetId,
                                                   @Validated @RequestBody UserBudgetUpdateRequest request) {
        String res = userBudgetService.updateUserBudget(userPrincipal.getUsername(), userBudgetId, request);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "유저예산 추천 조회 API")
    @GetMapping("/{budget}/recommend")
    public ResponseEntity<List<UserBudgetRecommendation>> getUserBudgetByRecommendation(@Parameter(description = "예산 입력값", required = true)
                                                                                        @PathVariable Long budget) {
        List<UserBudgetRecommendation> res = userBudgetService.getUserBudgetByRecommendation(budget);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "조회한 추천 예산으로 예산 설정 API")
    @PostMapping("/recommend")
    public ResponseEntity<String> createUserBudgetByRecommendation(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                   @RequestBody List<UserBudgetRecommendation> request) {
        String res = userBudgetServiceHandler.createUserBudgetByRecommendation(userPrincipal.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
