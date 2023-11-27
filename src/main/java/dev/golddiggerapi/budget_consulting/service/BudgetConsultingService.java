package dev.golddiggerapi.budget_consulting.service;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetConsultingService {

    private final ExpenditureCategoryRepository expenditureCategoryRepository;

    //TODO 고도화 필요
    public String analyzeBudgetStatus(Long realAvailableExpenditure) {
        if (realAvailableExpenditure < 0) {
            return "예산이 초과한 상태입니다. 쫌더 아껴서 쓰세요! 화이팅";
        }
        if (realAvailableExpenditure > 100_000L) {
            return "절약을 잘 실천하고 계세요! 오늘도 절약 도전!";
        }
        return "쫌 더 화이팅하면 골드를 캘 수 있습니다. 화이팅!";
    }

    public Long getMinimumAvailableExpenditure(String categoryName) {
        ExpenditureCategory expenditureCategory = expenditureCategoryRepository.findExpenditureCategoryByName(categoryName);
        return Long.valueOf(expenditureCategory.getMinimumExpenditurePerDay());
    }
}
