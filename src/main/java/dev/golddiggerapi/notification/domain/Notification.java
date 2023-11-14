package dev.golddiggerapi.notification.domain;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayRecommendationResponse;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayResponse;

public record Notification(
        String content
) {

    public static Notification toAnalyzeWebHook(ExpenditureByTodayResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("오늘 총 지출액은 ")
                .append(response.expenditureSum())
                .append("원 입니다.\n")
                .append("오늘의 적정 지출액은 ")
                .append(response.reasonableExpenditureSum())
                .append("\n")
                .append("카테고리별 사용 가능 금액" + "\n");

        response.expenditureByTodayByCategoryStatisticsResponses()
                .forEach(i -> {
                    sb.append("카테고리=").append(i.name()).append("\n");
                    sb.append("지출액=").append(i.expenditureSum()).append("원").append("\n");
                    sb.append("위험도=").append(i.risk()).append("\n");
                });

        return new Notification(sb.toString());
    }

    public static Notification toRecommendationWebHook(ExpenditureByTodayRecommendationResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("오늘 사용 가능 총액 지출 예산은 ")
                .append(response.availableTotalExpenditure())
                .append("원 입니다.\n")
                .append(response.message())
                .append("\n")
                .append("카테고리별 사용 가능 금액" + "\n");

        response.userBudgetCategoryAndAvailableExpenditureRecommendations()
                .forEach(i -> {
                    sb.append("카테고리=").append(i.name()).append("\n");
                    sb.append("지출가능금액=").append(i.availableExpenditure()).append("원").append("\n");
                });

        return new Notification(sb.toString());
    }
}
