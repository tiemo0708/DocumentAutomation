package project.DocumentAutomation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DocumentAutomation.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKey(String key);
}
