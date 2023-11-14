package dev.golddiggerapi.user.controller;

import dev.golddiggerapi.security.principal.UserPrincipal;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetRecommendation;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import dev.golddiggerapi.user.service.UserBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class UserBudgetController {

    private final UserBudgetService userBudgetService;

    @PostMapping("/{categoryId}")
    public ResponseEntity<String> createUserBudget(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @PathVariable Long categoryId,
                                                   @Validated @RequestBody UserBudgetCreateRequest request) {
        String res = userBudgetService.createUserBudget(userPrincipal.getUsername(), categoryId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping("/{userBudgetId}")
    public ResponseEntity<String> updateUserBudget(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @PathVariable Long userBudgetId,
                                                   @Validated @RequestBody UserBudgetUpdateRequest request) {
        String res = userBudgetService.updateUserBudget(userPrincipal.getUsername(), userBudgetId, request);
        return ResponseEntity.ok().body(res);
    }

    // 추천 예산을 조회합니다.
    @GetMapping("/{budget}/recommend")
    public ResponseEntity<List<UserBudgetRecommendation>> getUserBudgetByRecommendation(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                                        @PathVariable Long budget) {
        List<UserBudgetRecommendation> res = userBudgetService.getUserBudgetByRecommendation(userPrincipal.getUsername(), budget);
        return ResponseEntity.ok().body(res);
    }

    // 조회한 추천 예산으로 예산 설정.
    @PostMapping("/v1/recommend")
    public ResponseEntity<?> createUserBudgetByRecommendation(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              @RequestBody List<UserBudgetRecommendation> request) {
        String res = userBudgetService.createUserBudgetByRecommendation(userPrincipal.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
