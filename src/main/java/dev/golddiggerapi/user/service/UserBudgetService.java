package dev.golddiggerapi.user.service;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
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

    @Transactional
    public String createUserBudget(String username, Long categoryId, UserBudgetCreateRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("no category id in db"));

        UserBudget userBudget = new UserBudget(user, category, request);
        validateDuplicatedUserBudget(user, userBudget, category);
        userBudgetRepository.save(userBudget);
        return "created";
    }

    private boolean isExistsUserBudgetByCategoryAndMonth(User user, ExpenditureCategory category, LocalDateTime plannedMonth) {
        return userBudgetRepository.existsByUserAndExpenditureCategoryAndPlannedMonth(user, category, plannedMonth);
    }

    @Transactional
    public String updateUserBudget(String username, Long userBudgetId, UserBudgetUpdateRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        UserBudget userBudget = userBudgetRepository.findById(userBudgetId)
                .orElseThrow(() -> new IllegalArgumentException("no user budget in db"));

        ExpenditureCategory category = expenditureCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("no category id in db"));

        // 유저 예산의 카테고리, 년, 월, 예산총액을 수정할 수 있다.
        userBudget.update(request, category);
        // 업데이트후 중복된 월 and 카테고리 유저 예산이 DB에 있다면 예외처리한다.
        validateDuplicatedUserBudget(user, userBudget, category);
        return "updated";
    }

    private void validateDuplicatedUserBudget(User user, UserBudget userBudget, ExpenditureCategory category) {
        if (isExistsUserBudgetByCategoryAndMonth(user, category, userBudget.getPlannedMonth())) {
            throw new IllegalArgumentException("duplicated user budget category in month");
        }
    }

    public List<UserBudgetRecommendation> getUserBudgetByRecommendation(String username, Long budget) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

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

    @Transactional
    public String createUserBudgetByRecommendation(String username, List<UserBudgetRecommendation> request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        request.forEach(i -> {
            UserBudget userBudget = new UserBudget(user, i.category(), i.amount());
            userBudgetRepository.save(userBudget);
        });

        return "created by recommendation";
    }

    private boolean hasRemainingTotalRatio(AtomicReference<Double> totalRatio) {
        return totalRatio.get() > 0.00D;
    }
}