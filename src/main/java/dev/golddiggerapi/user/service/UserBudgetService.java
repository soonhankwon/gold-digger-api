package dev.golddiggerapi.user.service;

import dev.golddiggerapi.exception.CustomErrorCode;
import dev.golddiggerapi.exception.detail.ApiException;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import dev.golddiggerapi.global.util.service.TransactionService;
import dev.golddiggerapi.global.util.strategy.RedissonLockContext;
import dev.golddiggerapi.user.controller.dto.UserBudgetAvgRatioByCategoryStatisticResponse;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetRecommendation;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.domain.UserBudget;
import dev.golddiggerapi.user.repository.UserBudgetRepository;
import dev.golddiggerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class UserBudgetService {

    private final UserBudgetRepository userBudgetRepository;
    private final ExpenditureCategoryRepository expenditureCategoryRepository;
    private final UserRepository userRepository;
    private final RedissonLockContext redissonLockContext;
    private final TransactionService transactionService;

    public String createUserBudget(String username, Long categoryId, UserBudgetCreateRequest request) {
        redissonLockContext.executeLock(username, () ->
                // 락을 점유한 스레드만 트랜잭션 적용
                transactionService.executeAsTransactional(() -> {
                    User user = userRepository.findUserByUsername(username)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

                    ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.CATEGORY_NOT_FOUND_DB));

                    UserBudget userBudget = new UserBudget(user, category, request);
                    validateDuplicatedUserBudget(user, userBudget, category);
                    userBudgetRepository.save(userBudget);
                    return null;
                }));
        return "created";
    }

    private boolean isExistsUserBudgetByCategoryAndYearMonth(User user, ExpenditureCategory category, LocalDateTime plannedYearMonth) {
        return userBudgetRepository.existsByUserAndExpenditureCategoryAndPlannedYearMonth(user, category, plannedYearMonth);
    }

    @Transactional
    public String updateUserBudget(String username, Long userBudgetId, UserBudgetUpdateRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

        UserBudget userBudget = userBudgetRepository.findById(userBudgetId)
                .orElseThrow(() -> new ApiException(CustomErrorCode.USER_BUDGET_NOT_FOUND_DB));

        ExpenditureCategory category = expenditureCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ApiException(CustomErrorCode.CATEGORY_NOT_FOUND_DB));

        // 유저 예산의 카테고리, 년, 월, 예산총액을 수정할 수 있다.
        userBudget.update(request, category);
        // 업데이트후 중복된 월 and 카테고리 유저 예산이 DB에 있다면 예외처리한다.
        validateDuplicatedUserBudget(user, userBudget, category);
        return "updated";
    }

    private void validateDuplicatedUserBudget(User user, UserBudget userBudget, ExpenditureCategory category) {
        if (isExistsUserBudgetByCategoryAndYearMonth(user, category, userBudget.getPlannedYearMonth())) {
            throw new ApiException(CustomErrorCode.DUPLICATED_USER_BUDGET);
        }
    }

    public List<UserBudgetRecommendation> getUserBudgetByRecommendation(Long budget) {
        List<UserBudgetAvgRatioByCategoryStatisticResponse> statisticResponses =
                userBudgetRepository.statisticUserBudgetAvgRatioByCategory();

        List<UserBudgetRecommendation> res = new ArrayList<>();

        AtomicReference<Double> totalRatio = new AtomicReference<>(1.0D);
        statisticResponses.forEach(i -> {
            UserBudgetRecommendation userBudgetRecommendation = new UserBudgetRecommendation(i.category(), Math.round(budget * i.avgRatio()));
            res.add(userBudgetRecommendation);
            totalRatio.set(totalRatio.get() - i.avgRatio());
        });

        if (hasRemainingTotalRatio(totalRatio)) {
            ExpenditureCategory etcCategory = new ExpenditureCategory(10L, "기타");
            UserBudgetRecommendation userBudgetRecommendation = new UserBudgetRecommendation(etcCategory, Math.round(budget * totalRatio.get()));
            res.add(userBudgetRecommendation);
        }
        return res;
    }

    public String createUserBudgetByRecommendation(String username, List<UserBudgetRecommendation> request) {
        redissonLockContext.executeLock(username, () ->
                transactionService.executeAsTransactional(() -> {
                    User user = userRepository.findUserByUsername(username)
                            .orElseThrow(() -> new ApiException(CustomErrorCode.USER_NOT_FOUND_DB));

                    request.forEach(i -> {
                        UserBudget userBudget = new UserBudget(user, i.category(), i.amount());
                        validateDuplicatedUserBudget(user, userBudget, i.category());
                        userBudgetRepository.save(userBudget);
                    });
                    return null;
                }));
        return "created by recommendation";
    }

    private boolean hasRemainingTotalRatio(AtomicReference<Double> totalRatio) {
        return totalRatio.get() > 0.00D;
    }
}