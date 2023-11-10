package dev.golddiggerapi.user.controller;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import dev.golddiggerapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody UserSignupRequest request) {
        String res = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
