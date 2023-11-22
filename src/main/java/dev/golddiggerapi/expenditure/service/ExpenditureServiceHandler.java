package dev.golddiggerapi.expenditure.service;

import dev.golddiggerapi.budget_consulting.service.BudgetConsultingService;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayRecommendationResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureServiceHandler {

    private final BudgetConsultingService budgetConsultingService;
    private final ExpenditureService expenditureService;

    public ExpenditureServiceHandler(final BudgetConsultingService budgetConsultingService,
                                     final ExpenditureService expenditureService) {
        this.budgetConsultingService = budgetConsultingService;
        this.expenditureService = expenditureService;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendExpenditureRecommendationByToday() {
        expenditureService.sendExpenditureRecommendationByToday(
                budgetConsultingService::analyzeBudgetStatus,
                budgetConsultingService::getMinimumAvailableExpenditureV1
        );
    }

    public ExpenditureByTodayRecommendationResponse getExpenditureRecommendationByToday(String username) {
        return expenditureService.getExpenditureRecommendationByTodayV1(
                username,
                budgetConsultingService::analyzeBudgetStatus,
                budgetConsultingService::getMinimumAvailableExpenditureV1
        );
    }
}
