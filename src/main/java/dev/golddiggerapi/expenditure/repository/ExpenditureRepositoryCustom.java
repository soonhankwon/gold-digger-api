package dev.golddiggerapi.expenditure.repository;

import dev.golddiggerapi.expenditure.controller.dto.*;
import dev.golddiggerapi.statistics.controller.dto.ConsumptionRateByCategoryStatistics;
import dev.golddiggerapi.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenditureRepositoryCustom {
    List<ExpenditureCategoryAndAmountResponse> statisticExpenditureCategoryAndAmount(User user, ExpenditureByUserRequest request);

    List<ExpenditureMemoAndAmountResponse> getExpendituresMemoAndAmountByCondition(User user, ExpenditureByUserRequest request);

    ExpenditureMinAndMax getExpenditureMinAndMaxByUser(User user, ExpenditureByUserRequest request);

    Long getExpendituresSumByUserAndCondition(User user, ExpenditureByUserRequest request);

    List<UserExpenditureAvgRatioByCategoryStatisticResponse> statisticAvgRatioByCategory();

    List<ExpenditureCategoryAndAmountResponse> statisticExpenditureCategoryAndAmountByTodayByUser(User user);

    List<ConsumptionRateByCategoryStatistics> getExpenditureConsumptionRateByCategoryCompareToPreviousMonth();

    Long sumAmountByExpenditureDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
