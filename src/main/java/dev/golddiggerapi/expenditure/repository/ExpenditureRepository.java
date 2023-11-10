package dev.golddiggerapi.expenditure.repository;

import dev.golddiggerapi.expenditure.domain.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
}
