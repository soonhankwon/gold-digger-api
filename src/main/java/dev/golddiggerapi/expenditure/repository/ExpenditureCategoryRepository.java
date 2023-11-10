package dev.golddiggerapi.expenditure.repository;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenditureCategoryRepository extends JpaRepository<ExpenditureCategory, Long> {
}
