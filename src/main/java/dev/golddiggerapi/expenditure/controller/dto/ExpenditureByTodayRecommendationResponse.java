package dev.golddiggerapi.expenditure.controller.dto;

import dev.golddiggerapi.user.controller.dto.UserBudgetCategoryAndAvailableExpenditureRecommendation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "오늘 지출 추천 응답 DTO")
public record ExpenditureByTodayRecommendationResponse(

        @Schema(description = "오늘 지출 가능 총액", example = "37000")
        Long availableTotalExpenditure,

        @Schema(description = "메세지", example = "절약을 잘 실천하고 계세요! 오늘도 절약 도전!")
        String message,

        List<UserBudgetCategoryAndAvailableExpenditureRecommendation> userBudgetCategoryAndAvailableExpenditureRecommendations
) {
}
