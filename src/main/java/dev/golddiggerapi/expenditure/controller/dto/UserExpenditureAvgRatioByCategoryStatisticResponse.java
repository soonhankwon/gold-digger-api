package dev.golddiggerapi.expenditure.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 카테고리별 지출 평균 비율 (유저별 예산대비 지출 비율 기준) 통계 DTO")
public record UserExpenditureAvgRatioByCategoryStatisticResponse(

        @Schema(description = "카테고리 ID", example = "1")
        Long categoryId,

        @Schema(description = "카테고리 이름", example = "식비")
        String name,

        @Schema(description = "평균 비율", example = "0.32")
        Double avgRatio
) {
    @QueryProjection
    public UserExpenditureAvgRatioByCategoryStatisticResponse {
    }
}
