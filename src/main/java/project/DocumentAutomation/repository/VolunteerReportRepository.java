package project.DocumentAutomation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DocumentAutomation.domain.VolunteerReport;

public interface VolunteerReportRepository extends JpaRepository<VolunteerReport, Long> {
    // 필요에 따라 추가 쿼리 메서드 정의 가능
}
