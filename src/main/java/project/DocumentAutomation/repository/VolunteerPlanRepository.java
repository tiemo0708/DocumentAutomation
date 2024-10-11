package project.DocumentAutomation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DocumentAutomation.domain.VolunteerPlan;

import java.util.List;
import java.util.Optional;

public interface VolunteerPlanRepository extends JpaRepository<VolunteerPlan, Long> {
    // 특정 대학의 봉사 계획서를 ID로 조회
    Optional<VolunteerPlan> findByIdAndUniversity_UniversityName(Long id, String universityName);

    // 특정 대학의 모든 봉사 계획서를 조회
    List<VolunteerPlan> findAllByUniversity_UniversityName(String universityName);
}