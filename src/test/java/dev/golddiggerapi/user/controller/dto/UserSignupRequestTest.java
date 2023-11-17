package dev.golddiggerapi.user.controller.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserSignupRequestTest {

    UserSignupRequest request;

    @BeforeEach
    void init() {
        request = new UserSignupRequest("test-user", "password1!", true, "/api/url");
    }

    @Test
    void username() {
        assertThat(request.username()).isEqualTo("test-user");
    }

    @Test
    void password() {
        assertThat(request.password()).isEqualTo("password1!");
    }

    @Test
    void subscribeNotification() {
        assertThat(request.subscribeNotification()).isTrue();
    }

    @Test
    void discordUrl() {
        assertThat(request.discordUrl()).isEqualTo("/api/url");
    }
}