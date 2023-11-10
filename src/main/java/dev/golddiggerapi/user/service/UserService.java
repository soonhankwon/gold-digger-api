package dev.golddiggerapi.user.service;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public String createUser(UserSignupRequest request) {
        if(isExistsAccountName(request.accountName())) {
            throw new IllegalArgumentException("exist account name in db");
        }
        //TODO 인코더 Bean 등록 후 사용
        Function<String, String> encodeFunction =
                password -> new BCryptPasswordEncoder().encode(password);
        User user = new User(request, encodeFunction);
        userRepository.save(user);
        return "created";
    }

    private boolean isExistsAccountName(String accountName) {
        return userRepository.existsByAccountName(accountName);
    }
}
