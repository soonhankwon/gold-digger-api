package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "`user`", indexes = {
        @Index(name = "idx_username_idx", columnList = "username"),
        @Index(name = "idx_subscribe_notification_idx", columnList = "subscribe_notification")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    private String password;

    @Column(name = "subscribe_notification")
    private Boolean subscribeNotification;

    public User(UserSignupRequest request, Function<String, String> encoderFunction) {
        this.username = request.username();
        this.password = encoderFunction.apply(request.password());
        this.subscribeNotification = request.subscribeNotification();
    }
}