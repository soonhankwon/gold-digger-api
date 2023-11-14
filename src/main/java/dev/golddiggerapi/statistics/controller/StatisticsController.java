package dev.golddiggerapi.statistics.controller;

import dev.golddiggerapi.expenditure.controller.dto.UserExpenditureAvgRatioByCategoryStatisticResponse;
import dev.golddiggerapi.security.principal.UserPrincipal;
import dev.golddiggerapi.statistics.controller.dto.ExpenditureStatisticsResponse;
import dev.golddiggerapi.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
@Tag(name = "통계 API")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "지난달 대비 총액, 카테고리 별 소비율 & 지난 요일 대비 소비율(모든 기록 기준)" +
            "/ 다른 유저 대비 설정 예산 소비율 통계(로그인 유저 기준) API")
    @GetMapping("/expenditures/consumption-rate")
    public ResponseEntity<ExpenditureStatisticsResponse> getExpenditureStatistics(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        ExpenditureStatisticsResponse res = statisticsService.getExpenditureStatistics(userPrincipal.getUsername());
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "유저 카테고리별 지출 평균 비율 - 유저별 예산대비 지출 비율 기준 통계 API")
    @GetMapping("/expenditures/avg-ratio")
    public ResponseEntity<List<UserExpenditureAvgRatioByCategoryStatisticResponse>> statisticExpenditureAvgRatioByCategory() {
        List<UserExpenditureAvgRatioByCategoryStatisticResponse> res = statisticsService.statisticExpenditureAvgRatioByCategory();
        return ResponseEntity.ok().body(res);
    }
}
