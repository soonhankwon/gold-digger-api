package dev.golddiggerapi.user.repository;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.domain.UserBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserBudgetRepository extends JpaRepository<UserBudget, Long>, UserBudgetRepositoryCustom {
    boolean existsByUserAndExpenditureCategoryAndPlannedYearMonth(User user, ExpenditureCategory category, LocalDateTime plannedYearMonth);

    List<UserBudget> findUserBudgetsByUserAndPlannedYearMonth(User user, LocalDateTime plannedYearMonth);

    Optional<UserBudget> findUserBudgetByUserAndExpenditureCategory_IdAndPlannedYearMonth(User user, Long categoryId, LocalDateTime plannedYearMonth);
}
