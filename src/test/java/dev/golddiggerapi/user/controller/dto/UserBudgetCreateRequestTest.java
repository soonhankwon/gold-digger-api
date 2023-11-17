package dev.golddiggerapi.user.controller.dto;

import dev.golddiggerapi.exception.detail.ApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserBudgetCreateRequestTest {

    UserBudgetCreateRequest request = null;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void init() {
        request = new UserBudgetCreateRequest(1_000_000L, Year.now().getValue(), 1);
    }

    @Test
    void amount() {
        assertThat(request.amount()).isEqualTo(1_000_000L);
    }

    @Test
    void year() {
        assertThat(request.year()).isEqualTo(Year.now().getValue());
    }

    @Test
    void month() {
        assertThat(request.month()).isEqualTo(1);
    }

    @Test
    void year_before_now_year_throw_exception() {
        assertThatThrownBy(() -> new UserBudgetCreateRequest(1_000_000L, Year.now().minusYears(1L).getValue(), 1))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void amount_min_validation() {
        UserBudgetCreateRequest invalidRequest = new UserBudgetCreateRequest(-1L, Year.now().getValue(), 11);

        Set<ConstraintViolation<UserBudgetCreateRequest>> violations = validator.validate(invalidRequest);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void amount_max_validation() {
        UserBudgetCreateRequest invalidRequest = new UserBudgetCreateRequest(10_000_000_001L, Year.now().getValue(), 11);

        Set<ConstraintViolation<UserBudgetCreateRequest>> violations = validator.validate(invalidRequest);

        assertThat(violations.size()).isEqualTo(1);
    }


    @Test
    void year_max_validation() {
        UserBudgetCreateRequest invalidRequest = new UserBudgetCreateRequest(1_000_000L, 2101, 1);

        Set<ConstraintViolation<UserBudgetCreateRequest>> violations = validator.validate(invalidRequest);

        assertThat(violations.size()).isEqualTo(1);
    }
}