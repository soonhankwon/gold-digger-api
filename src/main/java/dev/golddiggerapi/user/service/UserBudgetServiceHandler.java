package dev.golddiggerapi.user.service;

import dev.golddiggerapi.global.annotation.RateLimit;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetRecommendation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBudgetServiceHandler {

    private final UserBudgetService userBudgetService;

    public UserBudgetServiceHandler(final UserBudgetService userBudgetService) {
        this.userBudgetService = userBudgetService;
    }

    @RateLimit
    public String createUserBudget(String username, Long categoryId, UserBudgetCreateRequest request) {
        userBudgetService.createUserBudget(username, categoryId, request);
        return "created";
    }

    @RateLimit
    public String createUserBudgetByRecommendation(String username, List<UserBudgetRecommendation> request) {
        userBudgetService.createUserBudgetByRecommendation(username, request);
        return "created by recommendation";
    }
}
