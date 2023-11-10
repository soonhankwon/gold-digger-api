package dev.golddiggerapi.user.repository;

import dev.golddiggerapi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByAccountName(String accountName);
}
