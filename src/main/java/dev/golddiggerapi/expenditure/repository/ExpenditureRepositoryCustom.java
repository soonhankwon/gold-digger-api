package dev.golddiggerapi.expenditure.repository;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByUserRequest;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureCategoryAndAmountResponse;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureMemoAndAmountResponse;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureMinAndMax;
import dev.golddiggerapi.user.domain.User;

import java.util.List;

public interface ExpenditureRepositoryCustom {
    List<ExpenditureCategoryAndAmountResponse> statisticExpenditureCategoryAndAmount(User user, ExpenditureByUserRequest request);
    List<ExpenditureMemoAndAmountResponse> getExpendituresMemoAndAmountByCondition(User user, ExpenditureByUserRequest request);
    ExpenditureMinAndMax getExpenditureMinAndMaxByUser(User user, ExpenditureByUserRequest request);
    Long getExpendituresSumByUserAndCondition(User user, ExpenditureByUserRequest request);
}
