package dev.golddiggerapi.user.repository;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.domain.UserBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserBudgetRepository extends JpaRepository<UserBudget, Long>, UserBudgetRepositoryCustom {
    boolean existsByUserAndExpenditureCategoryAndPlannedMonth(User user, ExpenditureCategory category, LocalDateTime plannedMonth);
}
