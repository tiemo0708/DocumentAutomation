package project.DocumentAutomation.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.DocumentAutomation.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}