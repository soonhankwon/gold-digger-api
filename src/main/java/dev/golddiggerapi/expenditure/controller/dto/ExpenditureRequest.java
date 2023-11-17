package dev.golddiggerapi.expenditure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "지출 요청 DTO")
public record ExpenditureRequest(
        //'yyyy-MM-dd HH:00' 형식 1차 validation -> ssss-wm-ei 2m:00 으로
        @Schema(description = "지출 일시", example = "2023-11-14 19:30")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:00", message = "yyyy-MM-dd HH:00 형식이어야 합니다.")
        String dateTime,

        @Schema(description = "지출액", example = "15000")
        @Min(value = 1, message = "지출액은 1보다 작을수 없습니다.")
        Long amount,

        @Schema(description = "메모", example = "점심")
        @Size(max = 100, message = "메모는 100자 이하로 작성되어야 합니다.")
        @NotNull(message = "메모는 null 일수 없습니다.")
        String memo
) {
}
