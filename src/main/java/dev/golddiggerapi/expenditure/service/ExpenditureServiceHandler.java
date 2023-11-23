package dev.golddiggerapi.expenditure.service;

import dev.golddiggerapi.budget_consulting.service.BudgetConsultingService;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayRecommendationResponse;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureRequest;
import dev.golddiggerapi.global.util.strategy.RedissonLockContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureServiceHandler {

    private final BudgetConsultingService budgetConsultingService;
    private final ExpenditureService expenditureService;
    private final RedissonLockContext redissonLockContext;

    public ExpenditureServiceHandler(final BudgetConsultingService budgetConsultingService,
                                     final ExpenditureService expenditureService,
                                     final RedissonLockContext redissonLockContext) {
        this.budgetConsultingService = budgetConsultingService;
        this.expenditureService = expenditureService;
        this.redissonLockContext = redissonLockContext;
    }

    public String createExpenditure(String username, Long categoryId, ExpenditureRequest request) {
        redissonLockContext.executeLock(username,
                () -> expenditureService.createExpenditure(username, categoryId, request));
        return "created";
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendExpenditureRecommendationByToday() {
        expenditureService.sendExpenditureRecommendationByToday(
                budgetConsultingService::analyzeBudgetStatus,
                budgetConsultingService::getMinimumAvailableExpenditure
        );
    }

    public ExpenditureByTodayRecommendationResponse getExpenditureRecommendationByToday(String username) {
        return expenditureService.getExpenditureRecommendationByToday(
                username,
                budgetConsultingService::analyzeBudgetStatus,
                budgetConsultingService::getMinimumAvailableExpenditure
        );
    }
}
