package dev.golddiggerapi.notification.domain;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayByCategoryStatisticsResponse;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayRecommendationResponse;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayResponse;
import dev.golddiggerapi.notification.event.ExpenditureAnalyzeEvent;
import dev.golddiggerapi.notification.event.ExpenditureRecommendationEvent;
import dev.golddiggerapi.user.controller.dto.UserBudgetCategoryAndAvailableExpenditureRecommendation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    Notification notification;

    @Test
    void toAnalyzeWebHook() {
        ExpenditureByTodayByCategoryStatisticsResponse statistics =
                new ExpenditureByTodayByCategoryStatisticsResponse(
                        1L,
                        "식비",
                        10_000L,
                        15_000L,
                        "150%");

        ExpenditureByTodayResponse expenditureByToday = new ExpenditureByTodayResponse(
                70_000L,
                35_000L,
                List.of(statistics)
        );
        ExpenditureAnalyzeEvent event = new ExpenditureAnalyzeEvent(expenditureByToday, "/api/url");

        Notification notificationDiscord = Notification.toAnalyzeWebHook(event);

        assertThat(notificationDiscord.content()).contains("오늘 총 지출액은");
    }

    @Test
    void toRecommendationWebHook() {
        UserBudgetCategoryAndAvailableExpenditureRecommendation expenditureRecommendation =
                new UserBudgetCategoryAndAvailableExpenditureRecommendation(1L, "식비", 12_000L);

        ExpenditureByTodayRecommendationResponse recommendation = new ExpenditureByTodayRecommendationResponse(
                50_000L,
                "오늘도 절약실천중 입니다!, 화이팅!",
                List.of(expenditureRecommendation)
        );
        ExpenditureRecommendationEvent event = new ExpenditureRecommendationEvent(recommendation, "/api/url");

        Notification notificationDiscord = Notification.toRecommendationWebHook(event);

        assertThat(notificationDiscord.content()).contains("오늘 사용 가능 총액 지출 예산은");
    }

    @Test
    void content() {
        notification = new Notification("알람", "/api/url");
        assertThat(notification.content()).isEqualTo("알람");
    }

    @Test
    void targetDiscordUrl() {
        notification = new Notification("알람", "/api/url");
        assertThat(notification.targetDiscordUrl()).isEqualTo("/api/url");
    }
}