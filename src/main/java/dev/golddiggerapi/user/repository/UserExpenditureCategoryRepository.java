package dev.golddiggerapi.user.repository;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.domain.UserExpenditureCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserExpenditureCategoryRepository extends JpaRepository<UserExpenditureCategory, Long> {
    Optional<UserExpenditureCategory> findUserExpenditureCategoryByUserAndExpenditureCategory(User user, ExpenditureCategory category);

    Optional<UserExpenditureCategory> findUserExpenditureCategoryByUserAndExpenditureCategory_Id(User user, Long categoryId);
}
