package dev.golddiggerapi.user.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UserSignupRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
