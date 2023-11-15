package dev.golddiggerapi.notification.event;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayResponse;

public record ExpenditureAnalyzeEvent(
        ExpenditureByTodayResponse expenditureByTodayResponse,
        String targetDiscordUrl
) {
}
