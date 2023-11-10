package dev.golddiggerapi.expenditure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.golddiggerapi.expenditure.controller.dto.*;
import dev.golddiggerapi.expenditure.domain.ExpenditureStatus;
import dev.golddiggerapi.expenditure.domain.QExpenditure;
import dev.golddiggerapi.expenditure.domain.QExpenditureCategory;
import dev.golddiggerapi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExpenditureRepositoryCustomImpl implements ExpenditureRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QExpenditure expenditure = QExpenditure.expenditure;
    QExpenditureCategory expenditureCategory = QExpenditureCategory.expenditureCategory;

    @Override
    public List<ExpenditureCategoryAndAmountResponse> statisticExpenditureCategoryAndAmount(User user, ExpenditureByUserRequest request) {
        if (request.getCategoryId() != null) {
            return queryFactory.select(new QExpenditureCategoryAndAmountResponse(expenditureCategory.id, expenditureCategory.name, expenditure.amount.sum()))
                    .from(expenditure)
                    .where(expenditure.user.eq(user)
                            .and(expenditureCategory.id.eq(request.getCategoryId()))
                            .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L)))
                            .and(expenditure.expenditureStatus.eq(ExpenditureStatus.INCLUDED)))
                    .fetch();
        }
        return queryFactory.select(new QExpenditureCategoryAndAmountResponse(expenditureCategory.id, expenditureCategory.name, expenditure.amount.sum()))
                .from(expenditure)
                .where(expenditure.user.eq(user)
                        .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L)))
                        .and(expenditure.expenditureStatus.eq(ExpenditureStatus.INCLUDED)))
                .groupBy(expenditureCategory.id)
                .fetch();
    }

    @Override
    public ExpenditureMinAndMax getExpenditureMinAndMaxByUser(User user, ExpenditureByUserRequest request) {
        if (request.getCategoryId() != null) {
            return queryFactory.select(new QExpenditureMinAndMax(expenditure.amount.min(), expenditure.amount.max()))
                    .from(expenditure)
                    .where(expenditure.user.eq(user)
                            .and(expenditureCategory.id.eq(request.getCategoryId()))
                            .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L)))
                            .and(expenditure.expenditureStatus.eq(ExpenditureStatus.INCLUDED)))
                    .fetchFirst();
        }
        return queryFactory.select(new QExpenditureMinAndMax(expenditure.amount.min(), expenditure.amount.max()))
                .from(expenditure)
                .where(expenditure.user.eq(user)
                        .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L)))
                        .and(expenditure.expenditureStatus.eq(ExpenditureStatus.INCLUDED)))
                .fetchFirst();
    }

    @Override
    public List<ExpenditureMemoAndAmountResponse> getExpendituresMemoAndAmountByCondition(User user, ExpenditureByUserRequest request) {
        if(request.getCategoryId() != null) {
            return queryFactory.select(new QExpenditureMemoAndAmountResponse(expenditureCategory.id, expenditure.memo, expenditure.amount))
                    .from(expenditure)
                    .where(expenditure.user.eq(user)
                            .and(expenditureCategory.id.eq(request.getCategoryId()))
                            .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L))))
                    .fetch();
        }
        return queryFactory.select(new QExpenditureMemoAndAmountResponse(expenditureCategory.id, expenditure.memo, expenditure.amount))
                .from(expenditure)
                .where(expenditure.user.eq(user)
                        .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L))))
                .fetch();
    }

    @Override
    public Long getExpendituresSumByUserAndCondition(User user, ExpenditureByUserRequest request) {
        return queryFactory.select(expenditure.amount.sum())
                .from(expenditure)
                .where(expenditure.user.eq(user)
                        .and(expenditure.expenditureDateTime.between(request.getStart(), request.getEnd().plusDays(1L)))
                        .and(expenditure.expenditureStatus.eq(ExpenditureStatus.INCLUDED)))
                .fetchFirst();
    }
}
