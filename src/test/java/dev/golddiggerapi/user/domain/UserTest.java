package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    UserSignupRequest request = null;
    Function<String, String> encoderFunction = s -> "encoded";

    @BeforeEach
    void init() {
        request = new UserSignupRequest("test-user", "password1!", true, "/api/url");
    }

    @Test
    void getId() {
        User user = new User(request, encoderFunction);
        assertThat(user.getId()).isNull();
    }

    @Test
    void getUsername() {
        User user = new User(request, encoderFunction);
        assertThat(user.getUsername()).isEqualTo("test-user");
    }

    @Test
    void getPassword() {
        User user = new User(request, encoderFunction);
        assertThat(user.getPassword()).isEqualTo("encoded");
    }

    @Test
    void getSubscribeNotification() {
        User user = new User(request, encoderFunction);
        assertThat(user.getSubscribeNotification()).isTrue();
    }

    @Test
    void getDiscordUrl() {
        User user = new User(request, encoderFunction);
        assertThat(user.getDiscordUrl()).isEqualTo("/api/url");
    }
}