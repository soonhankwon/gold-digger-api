package dev.golddiggerapi.user.service;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.domain.UserBudget;
import dev.golddiggerapi.user.repository.UserBudgetRepository;
import dev.golddiggerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return "updated";
    }
}
