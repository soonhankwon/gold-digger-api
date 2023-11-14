package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    public User(UserSignupRequest request, Function<String, String> encoderFunction) {
        this.username = request.username();
        this.password = encoderFunction.apply(request.password());
    }
}