package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;

    private String password;

    public User(UserSignupRequest request, Function<String, String> encoderFunction) {
        this.accountName = request.accountName();
        this.password = encoderFunction.apply(request.password());
    }
}
