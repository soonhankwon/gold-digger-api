package dev.golddiggerapi.notification.event;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayRecommendationResponse;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureByTodayResponse;
import dev.golddiggerapi.notification.domain.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NotificationEventListener {

    @Value("${discord.webhook.baseurl}")
    private String baseurl;

    @EventListener
    public void handleRecommendationNotificationEvent(ExpenditureRecommendationEvent event) {
        executeDiscordWebHook(Notification.toRecommendationWebHook(event));
    }

    @EventListener
    public void handleAnalyzeNotificationEvent(ExpenditureAnalyzeEvent event) {
        executeDiscordWebHook(Notification.toAnalyzeWebHook(event));
    }

    private void executeDiscordWebHook(Notification notification) {
        WebClient.create(baseurl)
                .post()
                .uri(notification.targetDiscordUrl())
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }
}
