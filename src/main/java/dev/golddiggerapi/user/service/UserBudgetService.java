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
    public String createUserBudget(String accountName, Long categoryId, UserBudgetCreateRequest request) {
        User user = userRepository.findUserByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        ExpenditureCategory category = expenditureCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("no category id in db"));

        UserBudget userBudget = new UserBudget(user, category, request);
        if (isExistsUserBudgetByCategory(user, category)) {
            throw new IllegalArgumentException("duplicated category");
        }
        userBudgetRepository.save(userBudget);
        return "created";
    }

    private boolean isExistsUserBudgetByCategory(User user, ExpenditureCategory category) {
        return userBudgetRepository.existsByUserAndExpenditureCategory(user, category);
    }

    @Transactional
    public String updateUserBudget(String accountName, Long userBudgetId, UserBudgetUpdateRequest request) {
        User user = userRepository.findUserByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("no account name in db"));

        UserBudget userBudget = userBudgetRepository.findById(userBudgetId)
                .orElseThrow(() -> new IllegalArgumentException("no user budget in db"));

        ExpenditureCategory category = expenditureCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("no category id in db"));

        userBudget.update(request, category);
        // 예산 카테고리를 변경했는데 이미 해당 카테고리의 예산이 있다면 예외 처리
        if (isExistsUserBudgetByCategory(user, category)) {
            throw new IllegalArgumentException("duplicated category");
        }
        return "updated";
    }

    public List<UserBudgetRecommendation> getUserBudgetByRecommendation(String accountName, Long budget) {
        User user = userRepository.findUserByAccountName(accountName)
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
    public String createUserBudgetByRecommendation(String accountName, List<UserBudgetRecommendation> request) {
        User user = userRepository.findUserByAccountName(accountName)
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