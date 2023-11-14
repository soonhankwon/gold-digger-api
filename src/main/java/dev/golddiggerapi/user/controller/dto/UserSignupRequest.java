package dev.golddiggerapi.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원 가입 요청 DTO")
public record UserSignupRequest(

        @NotBlank(message = "계정명은 하나 이상의 공백이 아닌 문자를 포함해야 합니다.")
        @Schema(description = "계정명", example = "abc")
        String username,

        @NotBlank(message = "패스워드는 하나 이상의 공백이 아닌 문자를 포함해야 합니다.")
        @Schema(description = "패스워드", example = "1234")
        String password,

        @NotBlank(message = "알람수신여부는 true 또는 false 여야 합니다.")
        @Schema(description = "알람수신여부", example = "true")
        Boolean subscribeNotification
) {
}
