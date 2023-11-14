package dev.golddiggerapi.statistics.service;

import dev.golddiggerapi.expenditure.repository.ExpenditureRepository;
import dev.golddiggerapi.statistics.controller.dto.ConsumptionRateByCategoryResponse;
import dev.golddiggerapi.statistics.controller.dto.ConsumptionRateByCategoryStatistics;
import dev.golddiggerapi.statistics.controller.dto.ExpenditureStatisticsResponse;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.repository.UserBudgetRepository;
import dev.golddiggerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserRepository userRepository;
    private final ExpenditureRepository expenditureRepository;
    private final UserBudgetRepository userBudgetRepository;

    public ExpenditureStatisticsResponse getExpenditureStatistics(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfLastMonth = YearMonth.now().minusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = now.minusMonths(1);
        LocalDateTime startOfThisMonth = YearMonth.now().atDay(1).atStartOfDay();

        // 지난 달 이 시점 대비 총액, 카테고리별 소비율 (얼마나 더 썼나?)
        Long previousMonthTotalPrice = expenditureRepository.sumAmountByExpenditureDateTimeBetween(startOfLastMonth, endOfLastMonth);
        Long thisMonthTotalPrice = expenditureRepository.sumAmountByExpenditureDateTimeBetween(startOfThisMonth, now);

        BiFunction<Long, Long, String> executeRatingToStringFunction = (a, b) -> String.valueOf((b / a) * 100) + '%';

        List<ConsumptionRateByCategoryStatistics> userBudgetConsumptionRateByCategoryCompareToPreviousMonth =
                expenditureRepository.getExpenditureConsumptionRateByCategoryCompareToPreviousMonth();

        List<ConsumptionRateByCategoryResponse> res = userBudgetConsumptionRateByCategoryCompareToPreviousMonth.stream()
                .map(ConsumptionRateByCategoryResponse::toResponse)
                .collect(Collectors.toList());
        // 지난 요일 대비 소비율 (얼마나 더 썼나?)
        // 현재 요일 & 현재 요일부터 7일 전의 요일
        DayOfWeek nowDay = now.getDayOfWeek();
        DayOfWeek previousDay = nowDay.minus(7);

        // 현재 요일의 시작과 끝
        LocalDateTime startOfToday = now.with(DayOfWeek.from(nowDay)).toLocalDate().atStartOfDay();
        LocalDateTime endOfToday = now.with(DayOfWeek.from(nowDay)).toLocalDate().atTime(23, 59, 59, 59);

        // 7일 전의 요일의 시작과 끝
        LocalDateTime startOfPreviousDay = now.with(DayOfWeek.from(previousDay)).toLocalDate().atStartOfDay();
        LocalDateTime endOfPreviousDay = now.with(DayOfWeek.from(previousDay)).toLocalDate().atTime(23, 59, 59, 59);

        Long previousDayTotalPrice = expenditureRepository.sumAmountByExpenditureDateTimeBetween(startOfPreviousDay, endOfPreviousDay);
        Long thisDayTotalPrice = expenditureRepository.sumAmountByExpenditureDateTimeBetween(startOfToday, endOfToday);

        // 다른 유저 대비 소비율 (오늘 기준 다른 유저가 예산 대비 사용한 평균 비율 대비 나의 비율)
        Long consumptionRateCompareByOtherUsers = userBudgetRepository.getUserBudgetConsumptionRateByUsers(user);
        Long consumptionRateByUser = userBudgetRepository.getUserBudgetConsumptionRateByUser(user);
        log.info("users={} me={}", consumptionRateCompareByOtherUsers, consumptionRateByUser);
        return new ExpenditureStatisticsResponse(
                executeRatingToStringFunction.apply(previousMonthTotalPrice, thisMonthTotalPrice),
                executeRatingToStringFunction.apply(previousDayTotalPrice, thisDayTotalPrice),
                executeRatingToStringFunction.apply(consumptionRateCompareByOtherUsers, consumptionRateByUser),
                res
        );
    }
}
