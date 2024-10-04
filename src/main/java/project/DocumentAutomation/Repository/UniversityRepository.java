package project.DocumentAutomation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DocumentAutomation.domain.University;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, String> {
    Optional<Object> findByUniversityName(String universityName);
}
