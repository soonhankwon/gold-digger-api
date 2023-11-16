package dev.golddiggerapi.user.controller;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import dev.golddiggerapi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "유저 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@Validated @RequestBody UserSignupRequest request) {
        String res = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
