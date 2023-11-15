package dev.golddiggerapi.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원 가입 요청 DTO")
public record UserSignupRequest(

        @NotBlank(message = "계정명은 하나 이상의 공백이 아닌 문자를 포함해야 합니다.")
        @Size(min = 6, max = 30, message = "계정명은 6자이상 30자이하로 작성해야합니다.")
        @Schema(description = "계정명", example = "username")
        String username,

        @NotBlank(message = "패스워드는 하나 이상의 공백이 아닌 문자를 포함해야 합니다.")
        @Size(min = 8, message = "패스워드는 최소 8글자 이상이어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).*$",
                message = "패스워드는 숫자, 문자, 특수 문자를 포함해야 합니다."
        )
        @Schema(description = "패스워드", example = "password1!")
        String password,

        @NotBlank(message = "알람수신여부는 true 또는 false 여야 합니다.")
        @Schema(description = "알람수신여부", example = "true")
        Boolean subscribeNotification,

        @Schema(description = "디스코드url", example = "/api/webhooks/...")
        String discordUrl
) {
}
