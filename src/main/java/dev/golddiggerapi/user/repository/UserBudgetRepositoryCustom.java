package dev.golddiggerapi.user.repository;

import dev.golddiggerapi.user.controller.dto.UserBudgetAvgRatioByCategoryStatisticResponse;

import java.util.List;

public interface UserBudgetRepositoryCustom {
    List<UserBudgetAvgRatioByCategoryStatisticResponse> statisticUserBudgetAvgRatioByCategory();
}
