package dev.golddiggerapi.budget_consulting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BudgetConsultingServiceTest {

    @InjectMocks
    BudgetConsultingService budgetConsultingService;

    @Test
    void analyzeBudgetStatus_isRealAvailableExpenditureUnderZero() {
        String analyzeMessage = budgetConsultingService.analyzeBudgetStatus(-1L);
        assertThat(analyzeMessage).isEqualTo("예산이 초과한 상태입니다. 쫌더 아껴서 쓰세요! 화이팅");
    }

    @Test
    void analyzeBudgetStatus_isRealAvailableExpenditureReasonable() {
        String analyzeMessage = budgetConsultingService.analyzeBudgetStatus(100_001L);
        assertThat(analyzeMessage).isEqualTo("절약을 잘 실천하고 계세요! 오늘도 절약 도전!");
    }

    @Test
    void analyzeBudgetStatus_isRealAvailableExpenditureLessReasonable() {
        String analyzeMessage = budgetConsultingService.analyzeBudgetStatus(99_999L);
        assertThat(analyzeMessage).isEqualTo("쫌 더 화이팅하면 골드를 캘 수 있습니다. 화이팅!");
    }
}