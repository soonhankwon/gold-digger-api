package dev.golddiggerapi.expenditure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "지출 업데이트 요청 DTO")
public record ExpenditureUpdateRequest(

        @Schema(description = "지출 일시", example = "2023-11-14 19:30")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:00")
        String dateTime,

        @Schema(description = "지출액", example = "45000")
        @Min(value = 1, message = "지출액은 1보다 작을수 없습니다.")
        Long amount,

        @Schema(description = "메모", example = "쇼핑")
        @NotNull(message = "메모는 null 일수 없습니다.")
        String memo,

        @Schema(description = "카테고리 ID", example = "2")
        @Min(value = 1, message = "카테고리 ID는 1이상이어야 합니다.")
        @NotNull(message = "카테고리 ID는 null 일수 없습니다.")
        Long categoryId
) {
}
