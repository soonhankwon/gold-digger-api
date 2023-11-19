package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureAnalyze;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_budget", indexes = {
        @Index(name = "fk_ub_user_idx", columnList = "user_id"),
        @Index(name = "fk_ub_expenditure_category_idx", columnList = "expenditure_category_id"),
        @Index(name = "idx_planned_year_month_idx", columnList = "planned_year_month")
})
public class UserBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    @Column(name = "planned_year_month")
    private LocalDateTime plannedYearMonth;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenditure_category_id")
    private ExpenditureCategory expenditureCategory;


    public UserBudget(User user, ExpenditureCategory category, UserBudgetCreateRequest request) {
        this.amount = request.amount();
        Integer year = request.year();
        Month month = Month.of(request.month());
        this.plannedYearMonth = YearMonth.of(year, month).atDay(1).atStartOfDay();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.user = user;
        this.expenditureCategory = category;
    }

    public UserBudget(User user, ExpenditureCategory category, Long amount) {
        this.amount = amount;
        this.plannedYearMonth = YearMonth.now().atDay(1).atStartOfDay();
        this.user = user;
        this.expenditureCategory = category;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(UserBudgetUpdateRequest request, ExpenditureCategory category) {
        this.amount = request.amount();
        Integer year = request.year();
        Month month = Month.of(request.month());
        this.plannedYearMonth = YearMonth.of(year, month).atDay(1).atStartOfDay();
        this.updatedAt = LocalDateTime.now();
        // Input 카테고리가 같지않다면 수정합니다.
        if (!isInputCategorySameAsThisCategory(category)) {
            this.expenditureCategory = category;
        }
    }

    private boolean isInputCategorySameAsThisCategory(ExpenditureCategory category) {
        return this.expenditureCategory == category;
    }

    public ExpenditureAnalyze analyzeReasonableExpenditureSumAndRisk(Long expenditureSum) {
        Long reasonableExpenditureSum = this.amount / YearMonth.now().lengthOfMonth();
        Long risk = (expenditureSum / reasonableExpenditureSum) * 100;
        return new ExpenditureAnalyze(reasonableExpenditureSum, risk);
    }
}
