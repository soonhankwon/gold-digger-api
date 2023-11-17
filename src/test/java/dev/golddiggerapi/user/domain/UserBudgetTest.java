package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureAnalyze;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class UserBudgetTest {

    UserBudgetCreateRequest request;
    UserBudget budget;
    User user;
    ExpenditureCategory category;

    @BeforeEach
    void init() {
        Function<String, String> encoderFunction = s -> "encoded";
        UserSignupRequest signupRequest = new UserSignupRequest("test-user", "password1!", true, "/api/url");
        user  = new User(signupRequest, encoderFunction);
        category = new ExpenditureCategory(1L, "식비");

        request = new UserBudgetCreateRequest(1_000_000L, 2023, 11);
        budget = new UserBudget(user, category, request);
    }

    @Test
    void getId() {
        assertThat(budget.getId()).isNull();
    }

    @Test
    void getAmount() {
        assertThat(budget.getAmount()).isEqualTo(1_000_000L);
    }

    @Test
    void getPlannedMonth() {
        Month month = Month.of(request.month());
        LocalDateTime requestPlannedMonth = YearMonth.of(request.year(), month).atDay(1).atStartOfDay();
        assertThat(budget.getPlannedYearMonth()).isEqualTo(requestPlannedMonth);
    }

    @Test
    void getCreatedAt() {
        assertThat(budget.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void getUpdatedAt() {
        assertThat(budget.getUpdatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void getUser() {
        assertThat(budget.getUser()).isSameAs(user);
    }

    @Test
    void getExpenditureCategory() {
        assertThat(budget.getExpenditureCategory()).isSameAs(category);
    }

    @Test
    void update() {
        UserBudgetUpdateRequest updateRequest = new UserBudgetUpdateRequest(90_000L, 2022, 1, 2L);
        ExpenditureCategory targetCategory = new ExpenditureCategory(updateRequest.categoryId(), "쇼핑");

        budget.update(updateRequest, targetCategory);

        assertThat(budget.getAmount()).isEqualTo(90_000L);
        assertThat(budget.getPlannedYearMonth()).isEqualTo(YearMonth.of(2022, Month.JANUARY).atDay(1).atStartOfDay());
        assertThat(budget.getExpenditureCategory()).isSameAs(targetCategory);
    }

    @Test
    void analyzeReasonableExpenditureSumAndRisk() {
        ExpenditureAnalyze expenditureAnalyze = budget.analyzeReasonableExpenditureSumAndRisk(10_000L);

        Long reasonableExpenditureSum = expenditureAnalyze.reasonableExpenditureSum();
        Long risk = expenditureAnalyze.risk();

        // (100만원 / 평균30일) = 약 하루 3.3만원 예산
        assertThat(reasonableExpenditureSum).isLessThan(35_000L);
        // 위험도 10_000L / 33_000L * 100 = 약 33%
        assertThat(risk).isLessThan(34);
    }
}