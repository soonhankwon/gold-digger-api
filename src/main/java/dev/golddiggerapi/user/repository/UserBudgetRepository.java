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
    boolean existsByUserAndExpenditureCategoryAndPlannedMonth(User user, ExpenditureCategory category, LocalDateTime plannedMonth);
    List<UserBudget> findUserBudgetsByUserAndPlannedMonth(User user, LocalDateTime plannedMonth);
    Optional<UserBudget> findUserBudgetByUserAndExpenditureCategory_IdAndPlannedMonth(User user, Long categoryId, LocalDateTime plannedMonth);
}
