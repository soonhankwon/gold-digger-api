package dev.golddiggerapi.user.service;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String createUser(UserSignupRequest request) {
        if(isExistsUsername(request.username())) {
            throw new IllegalArgumentException("exist account name in db");
        }

        Function<String, String> encodeFunction = passwordEncoder::encode;
        User user = new User(request, encodeFunction);
        userRepository.save(user);
        return "created";
    }

    private boolean isExistsUsername(String accountName) {
        return userRepository.existsByUsername(accountName);
    }
}
