package dev.golddiggerapi.statistics.controller;

import dev.golddiggerapi.statistics.controller.dto.ExpenditureStatisticsResponse;
import dev.golddiggerapi.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/expenditures/consumption-rate")
    public ResponseEntity<ExpenditureStatisticsResponse> getExpenditureStatistics() {
        String mockAccountName = "abc";
        ExpenditureStatisticsResponse res = statisticsService.getExpenditureStatistics(mockAccountName);
        return ResponseEntity.ok().body(res);
    }
}
