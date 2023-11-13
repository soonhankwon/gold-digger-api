package dev.golddiggerapi.user.repository;

import dev.golddiggerapi.user.controller.dto.UserBudgetAvgRatioByCategoryStatisticResponse;
import dev.golddiggerapi.user.controller.dto.UserBudgetCategoryAndAvailableExpenditure;
import dev.golddiggerapi.user.domain.User;

import java.util.List;

public interface UserBudgetRepositoryCustom {
    List<UserBudgetAvgRatioByCategoryStatisticResponse> statisticUserBudgetAvgRatioByCategory();

    List<UserBudgetCategoryAndAvailableExpenditure> getAvailableUserBudgetByCategoryByToday(User user);

    Long getUserBudgetConsumptionRateByUsers(User user);

    Long getUserBudgetConsumptionRateByUser(User user);
}
