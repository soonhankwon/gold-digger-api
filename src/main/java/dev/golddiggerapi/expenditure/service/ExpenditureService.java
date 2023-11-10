package dev.golddiggerapi.expenditure.service;

import dev.golddiggerapi.expenditure.controller.dto.*;
import dev.golddiggerapi.expenditure.domain.Expenditure;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import dev.golddiggerapi.expenditure.repository.ExpenditureRepository;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;
    private final UserRepository userRepository;
    private final ExpenditureCategoryRepository expenditureCategoryRepository;

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
}
