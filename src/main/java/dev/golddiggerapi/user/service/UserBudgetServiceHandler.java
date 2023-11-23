package dev.golddiggerapi.user.service;

import dev.golddiggerapi.global.util.strategy.RedissonLockContext;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetRecommendation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBudgetServiceHandler {

    private final UserBudgetService userBudgetService;
    private final RedissonLockContext redissonLockContext;

    public UserBudgetServiceHandler(final UserBudgetService userBudgetService,
                                    final RedissonLockContext redissonLockContext) {
        this.userBudgetService = userBudgetService;
        this.redissonLockContext = redissonLockContext;
    }

    public String createUserBudget(String username, Long categoryId, UserBudgetCreateRequest request) {
        redissonLockContext.executeLock(username,
                () -> userBudgetService.createUserBudget(username, categoryId, request));
        return "created";
    }

    public String createUserBudgetByRecommendation(String username, List<UserBudgetRecommendation> request) {
        redissonLockContext.executeLock(username,
                () -> userBudgetService.createUserBudgetByRecommendation(username, request));
        return "created by recommendation";
    }
}
