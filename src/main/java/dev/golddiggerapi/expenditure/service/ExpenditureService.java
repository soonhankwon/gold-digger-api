package dev.golddiggerapi.expenditure.service;

import dev.golddiggerapi.budget_consulting.service.BudgetConsultingService;
import dev.golddiggerapi.expenditure.controller.dto.*;
import dev.golddiggerapi.expenditure.domain.Expenditure;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import dev.golddiggerapi.expenditure.repository.ExpenditureRepository;
import dev.golddiggerapi.notification.event.ExpenditureAnalyzeEvent;
import dev.golddiggerapi.notification.event.ExpenditureRecommendationEvent;
import dev.golddiggerapi.user.controller.dto.UserBudgetCategoryAndAvailableExpenditure;
import dev.golddiggerapi.user.controller.dto.UserBudgetCategoryAndAvailableExpenditureRecommendation;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.domain.UserBudget;
import dev.golddiggerapi.user.repository.UserBudgetRepository;
import dev.golddiggerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;
    private final UserRepository userRepository;
    private final ExpenditureCategoryRepository expenditureCategoryRepository;
    private final UserBudgetRepository userBudgetRepository;
    private final BudgetConsultingService budgetConsultingService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public String createExpenditure(String username, Long categoryId, ExpenditureRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("no category id in db"));

        Expenditure expenditure = new Expenditure(user, category, request);
        expenditureRepository.save(expenditure);
        return "created";
    }

    @Transactional
    public String updateExpenditure(String username, Long expenditureId, ExpenditureUpdateRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        ExpenditureCategory category = expenditureCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("no category id in db"));

        Expenditure expenditure = expenditureRepository.findExpenditureByIdAndUser(expenditureId, user)
                .orElseThrow(() -> new IllegalArgumentException("no expenditure id by user"));

        // 지출액 업데이트
        expenditure.update(request, category);
        return "updated";
    }

    public ExpenditureResponse getExpenditure(String username, Long expenditureId) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        Expenditure expenditure = expenditureRepository.findExpenditureByIdAndUser(expenditureId, user)
                .orElseThrow(() -> new IllegalArgumentException("no expenditure id by user"));

        return ExpenditureResponse.toResponse(expenditure);
    }

    public ExpenditureByUserResponse getExpendituresByUser(String username, ExpenditureByUserRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));
        // 카테고리 ID가 없을 경우 : 전체 카테고리 목록 조회
        List<ExpenditureCategoryAndAmountResponse> response = expenditureRepository.statisticExpenditureCategoryAndAmount(user, request);
        // 각 지출 메모와 액수를 조회한다.
        List<ExpenditureMemoAndAmountResponse> memoAndAmount = expenditureRepository.getExpendituresMemoAndAmountByCondition(user, request);
        if (!hasCategoryId(request)) {
            Long sum = expenditureRepository.getExpendituresSumByUserAndCondition(user, request);
            if (hasRequestMinAndMax(request)) {
                ExpenditureMinAndMax minAndMax = expenditureRepository.getExpenditureMinAndMaxByUser(user, request);
                return new ExpenditureByUserResponse(sum, minAndMax, response, memoAndAmount);
            }
            return new ExpenditureByUserResponse(sum, null, response, memoAndAmount);
        }
        // 카테고리 ID가 있을 경우 : 특정 카테고리 목록 조회
        if (hasRequestMinAndMax(request)) {
            ExpenditureMinAndMax minAndMax = expenditureRepository.getExpenditureMinAndMaxByUser(user, request);
            return new ExpenditureByUserResponse(response.get(0).sum(), minAndMax, response, memoAndAmount);
        }
        return new ExpenditureByUserResponse(response.get(0).sum(), null, response, memoAndAmount);
    }

    private boolean hasCategoryId(ExpenditureByUserRequest request) {
        return request.getCategoryId() != null;
    }

    private boolean hasRequestMinAndMax(ExpenditureByUserRequest request) {
        return request.getHasMinAndMax();
    }

    @Transactional
    public String deleteExpenditure(String username, Long expenditureId) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        Expenditure expenditure = expenditureRepository.findExpenditureByIdAndUser(expenditureId, user)
                .orElseThrow(() -> new IllegalArgumentException("no expenditure in db or no auth to delete"));

        expenditureRepository.delete(expenditure);
        return "deleted";
    }

    @Transactional
    public String excludeExpenditure(String username, Long expenditureId) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        Expenditure expenditure = expenditureRepository.findExpenditureByIdAndUser(expenditureId, user)
                .orElseThrow(() -> new IllegalArgumentException("no expenditure in db or no auth to delete"));

        expenditure.exclude();
        return "excluded";
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendExpenditureRecommendationByToday() {
        List<User> usersBySubscribeNotification = userRepository.findAllBySubscribeNotification(Boolean.TRUE);
        usersBySubscribeNotification.forEach(i -> {
            ExpenditureByTodayRecommendationResponse expenditureRecommendationByToday = getExpenditureRecommendationByToday(i.getUsername());
            applicationEventPublisher.publishEvent(new ExpenditureRecommendationEvent(expenditureRecommendationByToday));
        });
    }

    public ExpenditureByTodayRecommendationResponse getExpenditureRecommendationByToday(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        // 이번달의 어제까지의 카테고리별 지출을 예산에 반영해야한다.
        // 유저의 오늘 지출 가능한 금액 총액, 카테고리별 금액
        List<UserBudgetCategoryAndAvailableExpenditure> availableUserBudgetByCategoryByToday = userBudgetRepository.getAvailableUserBudgetByCategoryByToday(user);

        Long realAvailableExpenditure = availableUserBudgetByCategoryByToday.stream()
                .mapToLong(UserBudgetCategoryAndAvailableExpenditure::availableExpenditure).sum();
        // 예산 컨설팅 서비스에서 실제 예산 대비 지출액으로 구체적인 분석 담당
        String message = budgetConsultingService.analyzeBudgetStatus(realAvailableExpenditure);

        // 지속적인 소비 습관을 생성하기 위한 서비스이므로 예산을 초과하더라도 적정한 금액을 추천
        List<UserBudgetCategoryAndAvailableExpenditureRecommendation> res =
                availableUserBudgetByCategoryByToday.stream()
                        .map(i -> {
                            if (i.availableExpenditure() < 0) {
                                Long minimumAvailableExpenditure = budgetConsultingService.getMinimumAvailableExpenditure(i);
                                return UserBudgetCategoryAndAvailableExpenditureRecommendation.toMinimumRecommendation(i, minimumAvailableExpenditure);
                            }
                            return UserBudgetCategoryAndAvailableExpenditureRecommendation.toRecommendation(i);
                        })
                        .toList();

        Long availableTotalExpenditure = res.stream()
                .mapToLong(UserBudgetCategoryAndAvailableExpenditureRecommendation::availableExpenditure)
                .sum();

        return new ExpenditureByTodayRecommendationResponse(
                availableTotalExpenditure,
                message,
                res
        );
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void sendExpenditureByToday() {
        List<User> usersBySubscribeNotification = userRepository.findAllBySubscribeNotification(Boolean.TRUE);
        usersBySubscribeNotification.forEach(i -> {
            ExpenditureByTodayResponse expenditureByToday = getExpenditureByToday(i.getUsername());
            applicationEventPublisher.publishEvent(new ExpenditureAnalyzeEvent(expenditureByToday));
        });
    }

    public ExpenditureByTodayResponse getExpenditureByToday(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));
        // 유저의 카테고리별 오늘 지출 통계 결과를 가져온다.
        List<ExpenditureCategoryAndAmountResponse> expenditureCategoryAndAmountResponses = expenditureRepository.statisticExpenditureCategoryAndAmountByTodayByUser(user);

        // 유저의 오늘 지출 총합
        Long sum = expenditureCategoryAndAmountResponses.stream()
                .mapToLong(ExpenditureCategoryAndAmountResponse::sum)
                .sum();

        // 유저의 이번달 설정 예산을 가져온다.
        List<UserBudget> userBudgetsInNowMonth = userBudgetRepository.findUserBudgetsByUserAndPlannedMonth(user, YearMonth.now().atDay(1).atStartOfDay());

        // 유저의 이번달 설정 예산 총합
        long plannedBudget = userBudgetsInNowMonth
                .stream()
                .mapToLong(UserBudget::getAmount)
                .sum();

        // 유저의 이번달 하루 적절 지출 금액 (총 예산 기준)
        Long reasonableExpenditurePerDay = plannedBudget / YearMonth.now().lengthOfMonth();

        // 기존 카테고리별 통계자료에 일자기준 오늘 적정 지출 금액과 위험도를 분석해서 응답을 만든다.
        // 유저 설정 예산이 없는 경우 0, 0 분석결과를 반환한다.
        List<ExpenditureByTodayByCategoryStatisticsResponse> expenditureByTodayByCategoryStatisticsResponses = new ArrayList<>();
        expenditureCategoryAndAmountResponses.stream()
                .map(i -> {
                    Optional<UserBudget> optionalUserBudget =
                            userBudgetRepository.findUserBudgetByUserAndExpenditureCategory_IdAndPlannedMonth(user, i.categoryId(), YearMonth.now().atDay(1).atStartOfDay());
                    return optionalUserBudget.map(userBudget ->
                                    ExpenditureByTodayByCategoryStatisticsResponse.toResponse(i, userBudget.analyzeReasonableExpenditureSumAndRisk(i.sum())))
                            .orElseGet(() -> ExpenditureByTodayByCategoryStatisticsResponse.toResponse(i, new ExpenditureAnalyze(0L, 0L)));
                })
                .forEach(expenditureByTodayByCategoryStatisticsResponses::add);

        return new ExpenditureByTodayResponse(
                sum,
                reasonableExpenditurePerDay,
                expenditureByTodayByCategoryStatisticsResponses);
    }

    public List<UserExpenditureAvgRatioByCategoryStatisticResponse> statisticExpenditureAvgRatioByCategory() {
        return expenditureRepository.statisticAvgRatioByCategory();
    }
}
