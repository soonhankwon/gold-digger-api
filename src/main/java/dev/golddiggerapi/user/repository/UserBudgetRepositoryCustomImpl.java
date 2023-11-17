package dev.golddiggerapi.user.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.golddiggerapi.expenditure.domain.QExpenditure;
import dev.golddiggerapi.expenditure.domain.QExpenditureCategory;
import dev.golddiggerapi.user.controller.dto.QUserBudgetAvgRatioByCategoryStatisticResponse;
import dev.golddiggerapi.user.controller.dto.QUserBudgetCategoryAndAvailableExpenditure;
import dev.golddiggerapi.user.controller.dto.UserBudgetAvgRatioByCategoryStatisticResponse;
import dev.golddiggerapi.user.controller.dto.UserBudgetCategoryAndAvailableExpenditure;
import dev.golddiggerapi.user.domain.QUserBudget;
import dev.golddiggerapi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserBudgetRepositoryCustomImpl implements UserBudgetRepositoryCustom {

    QExpenditureCategory expenditureCategory = QExpenditureCategory.expenditureCategory;
    QExpenditure expenditure = QExpenditure.expenditure;
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

    @Override
    public List<UserBudgetCategoryAndAvailableExpenditure> getAvailableUserBudgetByCategoryByToday(User user) {
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime endOfThisMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);

        long totalDaysThisMonth = ChronoUnit.DAYS.between(startOfMonth, endOfThisMonth) + 1;
        long daysPassed = ChronoUnit.DAYS.between(startOfMonth, yesterday) + 1;
        long remainingDays = totalDaysThisMonth - daysPassed;

        return queryFactory.select(new QUserBudgetCategoryAndAvailableExpenditure(expenditureCategory.id, expenditureCategory.name,
                        (userBudget.amount.sum().subtract(
                                JPAExpressions.select(expenditure.amount.sum())
                                        .from(expenditure)
                                        .where(expenditure.expenditureCategory.eq(expenditureCategory)
                                                .and(expenditure.user.eq(user))
                                                .and(expenditure.expenditureDateTime
                                                        .between(startOfMonth, yesterday))))
                        ).divide(remainingDays > 0 ? remainingDays : 1L)))
                .from(userBudget)
                .where(userBudget.user.eq(user)
                        .and(userBudget.plannedMonth.eq(startOfMonth)))
                .groupBy(expenditureCategory.id)
                .orderBy(expenditureCategory.id.asc())
                .fetch();
    }

    @Override
    public Long getUserBudgetConsumptionRateByUsers(User user) {
        Long result = queryFactory.select(expenditure.amount.sum().divide(
                        JPAExpressions.select(userBudget.amount.sum())
                                .from(userBudget)
                                .where(userBudget.user.ne(user))
                ).multiply(100))
                .from(expenditure)
                .where(expenditure.expenditureDateTime.between(YearMonth.now().atDay(1).atStartOfDay(), LocalDateTime.now())
                        .and(expenditure.user.ne(user)))
                .fetchFirst();
        return result != null ? result : 1L;
    }

    @Override
    public Long getUserBudgetConsumptionRateByUser(User user) {
        Long result = queryFactory.select(expenditure.amount.sum().divide(
                        JPAExpressions.select(userBudget.amount.sum())
                                .from(userBudget)
                                .where(userBudget.user.eq(user))
                ).multiply(100))
                .from(expenditure)
                .where(expenditure.expenditureDateTime.between(YearMonth.now().atDay(1).atStartOfDay(), LocalDateTime.now())
                        .and(expenditure.user.eq(user)))
                .fetchFirst();
        return result != null ? result : 1L;
    }
}
