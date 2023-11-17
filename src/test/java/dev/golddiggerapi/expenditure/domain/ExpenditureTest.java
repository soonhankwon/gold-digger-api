package dev.golddiggerapi.expenditure.domain;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureRequest;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureUpdateRequest;
import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import dev.golddiggerapi.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenditureTest {

    Expenditure expenditure;
    User user;
    ExpenditureCategory category;

    @BeforeEach
    void init() {
        UserSignupRequest signupRequest = new UserSignupRequest("test-user", "password1!", true, "/api/url");
        Function<String, String> encoderFunction = s -> "encoded";
        user  = new User(signupRequest, encoderFunction);

        ExpenditureRequest request = new ExpenditureRequest("2023-11-14 13:00", 10_000L, "점심");
        category = new ExpenditureCategory(1L, "식비");

        expenditure = new Expenditure(user, category, request);
    }

    @Test
    void getId() {
        assertThat(expenditure.getId()).isNull();
    }

    @Test
    void getAmount() {
        assertThat(expenditure.getAmount()).isEqualTo(10_000L);
    }

    @Test
    void getMemo() {
        assertThat(expenditure.getMemo()).isEqualTo("점심");
    }

    @Test
    void getExpenditureDateTime() {
        assertThat(expenditure.getExpenditureDateTime())
                .isEqualTo(LocalDateTime.of(2023, Month.NOVEMBER, 14, 13, 0));
    }

    @Test
    void getExpenditureStatus() {
        assertThat(expenditure.getExpenditureStatus()).isEqualTo(ExpenditureStatus.INCLUDED);
    }

    @Test
    void getCreatedAt() {
        assertThat(expenditure.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void getUpdatedAt() {
        assertThat(expenditure.getUpdatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void getUser() {
        assertThat(expenditure.getUser()).isEqualTo(user);
    }

    @Test
    void getExpenditureCategory() {
        assertThat(expenditure.getExpenditureCategory()).isEqualTo(category);
    }

    @Test
    void update() {
        ExpenditureUpdateRequest request = new ExpenditureUpdateRequest("2023-12-24 13:00", 50_000L, "선물 구매", 2L);
        ExpenditureCategory targetCategory = new ExpenditureCategory(request.categoryId(), "쇼핑");

        expenditure.update(request, targetCategory);

        assertThat(expenditure.getExpenditureDateTime())
                .isEqualTo(LocalDateTime.of(2023, Month.DECEMBER, 24, 13, 0));
        assertThat(expenditure.getAmount())
                .isEqualTo(50_000L);
        assertThat(expenditure.getExpenditureCategory())
                .isEqualTo(targetCategory);
    }

    @Test
    void exclude() {
        expenditure.exclude();
        assertThat(expenditure.getExpenditureStatus()).isSameAs(ExpenditureStatus.EXCLUDED);
    }
}