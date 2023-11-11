package dev.golddiggerapi.user.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.golddiggerapi.expenditure.domain.QExpenditureCategory;
import dev.golddiggerapi.user.controller.dto.QUserBudgetAvgRatioByCategoryStatisticResponse;
import dev.golddiggerapi.user.controller.dto.UserBudgetAvgRatioByCategoryStatisticResponse;
import dev.golddiggerapi.user.domain.QUserBudget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserBudgetRepositoryCustomImpl implements UserBudgetRepositoryCustom {

    QExpenditureCategory expenditureCategory = QExpenditureCategory.expenditureCategory;
    QUserBudget userBudget = QUserBudget.userBudget;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserBudgetAvgRatioByCategoryStatisticResponse> statisticUserBudgetAvgRatioByCategory() {
        return queryFactory.select(
                new QUserBudgetAvgRatioByCategoryStatisticResponse(expenditureCategory,
                        userBudget.amount.sum().divide(
                                JPAExpressions.select(userBudget.amount.sum())
                                        .from(userBudget)
                                        .where(userBudget.expenditureCategory.eq(expenditureCategory)))
                                .doubleValue()))
                .from(userBudget)
                .join(userBudget.expenditureCategory).on(userBudget.expenditureCategory.eq(expenditureCategory))
                .groupBy(expenditureCategory)
                .having(userBudget.amount.sum().divide(
                                JPAExpressions.select(userBudget.amount.sum())
                                        .from(userBudget)
                                        .where(userBudget.expenditureCategory.eq(expenditureCategory)))
                        .doubleValue().gt(0.10))
                .fetch();
    }
}
