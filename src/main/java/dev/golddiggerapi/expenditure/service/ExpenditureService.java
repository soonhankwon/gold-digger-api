package dev.golddiggerapi.expenditure.service;

import dev.golddiggerapi.expenditure.controller.dto.*;
import dev.golddiggerapi.expenditure.domain.Expenditure;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import dev.golddiggerapi.expenditure.repository.ExpenditureRepository;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.domain.UserBudget;
import dev.golddiggerapi.user.repository.UserBudgetRepository;
import dev.golddiggerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public String createExpenditure(String accountName, Long categoryId, ExpenditureRequest request) {
        User user = userRepository.findUserByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("no category id in db"));

        Expenditure expenditure = new Expenditure(user, category, request);
        expenditureRepository.save(expenditure);
        return "created";
    }

    @Transactional
    public String updateExpenditure(String accountName, Long expenditureId, ExpenditureUpdateRequest request) {
        User user = userRepository.findUserByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        ExpenditureCategory category = expenditureCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("no category id in db"));

        Expenditure expenditure = expenditureRepository.findById(expenditureId)
                .orElseThrow(() -> new IllegalArgumentException("no expenditure in db"));

        // 지출액 업데이트
        expenditure.update(request, category);
        return "updated";
    }

    public ExpenditureResponse getExpenditure(String accountName, Long expenditureId) {
        User user = userRepository.findUserByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        Expenditure expenditure = expenditureRepository.findById(expenditureId)
                .orElseThrow(() -> new IllegalArgumentException("no expenditure in db"));

        return ExpenditureResponse.toResponse(expenditure);
    }

    public ExpenditureByUserResponse getExpendituresByUser(String accountName, ExpenditureByUserRequest request) {
        User user = userRepository.findUserByAccountName(accountName)
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
    public String deleteExpenditure(String accountName, Long expenditureId) {
        User user = userRepository.findUserByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        Expenditure expenditure = expenditureRepository.findExpenditureByIdAndUser(expenditureId, user)
                .orElseThrow(() -> new IllegalArgumentException("no expenditure in db or no auth to delete"));

        expenditureRepository.delete(expenditure);
        return "deleted";
    }

    @Transactional
    public String excludeExpenditure(String accountName, Long expenditureId) {
        User user = userRepository.findUserByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        Expenditure expenditure = expenditureRepository.findExpenditureByIdAndUser(expenditureId, user)
                .orElseThrow(() -> new IllegalArgumentException("no expenditure in db or no auth to delete"));

        expenditure.exclude();
        return "excluded";
    }

    public ExpenditureByTodayResponse getExpenditureByToday(String accountName) {
        User user = userRepository.findUserByAccountName(accountName)
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

    public List<UserExpenditureAvgRatioByCategoryStatisticResponse> statisticExpenditureAvgRatioByCategory(String accountName) {
        User user = userRepository.findUserByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        List<UserExpenditureAvgRatioByCategoryStatisticResponse> res = expenditureRepository.statisticAvgRatioByCategory();
        return res;
    }
}