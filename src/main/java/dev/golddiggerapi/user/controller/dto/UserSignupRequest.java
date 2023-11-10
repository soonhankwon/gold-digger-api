package dev.golddiggerapi.user.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UserSignupRequest(
        @NotBlank
        String accountName,
        @NotBlank
        String password
) {
}
