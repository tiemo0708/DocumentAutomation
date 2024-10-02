package project.DocumentAutomation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DocumentAutomation.domain.University;

public interface UniversityRepository extends JpaRepository<University, String> {
}
