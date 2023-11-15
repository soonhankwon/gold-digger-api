package dev.golddiggerapi.notification.event;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayRecommendationResponse;

public record ExpenditureRecommendationEvent(
        ExpenditureByTodayRecommendationResponse expenditureByTodayRecommendationResponse,
        String targetDiscordUrl
) {
}
