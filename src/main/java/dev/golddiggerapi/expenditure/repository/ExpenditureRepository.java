package dev.golddiggerapi.expenditure.repository;

import dev.golddiggerapi.expenditure.domain.Expenditure;
import dev.golddiggerapi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure, Long>, ExpenditureRepositoryCustom{
    Optional<Expenditure> findExpenditureByIdAndUser(Long id, User user);
}
