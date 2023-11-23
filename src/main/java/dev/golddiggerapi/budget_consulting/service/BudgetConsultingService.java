package dev.golddiggerapi.budget_consulting.service;

import org.springframework.stereotype.Service;

@Service
public class BudgetConsultingService {

    //TODO 고도화 필요
    public String analyzeBudgetStatus(Long realAvailableExpenditure) {
        if(realAvailableExpenditure < 0) {
            return "예산이 초과한 상태입니다. 쫌더 아껴서 쓰세요! 화이팅";
        }
        if(realAvailableExpenditure > 100_000L) {
            return "절약을 잘 실천하고 계세요! 오늘도 절약 도전!";
        }
        return "쫌 더 화이팅하면 골드를 캘 수 있습니다. 화이팅!";
    }

    //TODO 데이터 통계 기반으로 고도화 필요
    public Long getMinimumAvailableExpenditure(String categoryName) {
        if(categoryName.equals("식비")) {
            return 15_000L;
        }
        return 10_000L;
    }
}
