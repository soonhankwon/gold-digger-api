package dev.golddiggerapi.user.controller;

import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetRecommendation;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import dev.golddiggerapi.user.service.UserBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class UserBudgetController {

    private final UserBudgetService userBudgetService;

    @PostMapping("/{categoryId}")
    public ResponseEntity<String> createUserBudget(@PathVariable Long categoryId,
                                                   @Validated @RequestBody UserBudgetCreateRequest request) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc";
        String res = userBudgetService.createUserBudget(mockAccountName, categoryId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping("/{userBudgetId}")
    public ResponseEntity<String> updateUserBudget(@PathVariable Long userBudgetId,
                                                   @Validated @RequestBody UserBudgetUpdateRequest request) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc";
        String res = userBudgetService.updateUserBudget(mockAccountName, userBudgetId, request);
        return ResponseEntity.ok().body(res);
    }

    // 추천 예산을 조회합니다.
    @GetMapping("/{budget}/recommend")
    public ResponseEntity<List<UserBudgetRecommendation>> getUserBudgetByRecommendation(@PathVariable Long budget) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc4";
        List<UserBudgetRecommendation> res = userBudgetService.getUserBudgetByRecommendation(mockAccountName, budget);
        return ResponseEntity.ok().body(res);
    }

    // 조회한 추천 예산으로 예산 설정.
    @PostMapping("/v1/recommend")
    public ResponseEntity<?> createUserBudgetByRecommendation(@RequestBody List<UserBudgetRecommendation> request) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc4";
        String res = userBudgetService.createUserBudgetByRecommendation(mockAccountName, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
