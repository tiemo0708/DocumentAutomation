package project.DocumentAutomation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DocumentAutomation.domain.University;
import project.DocumentAutomation.domain.UniversityMember;

import java.util.List;
import java.util.Optional;

public interface UniversityMemberRepository extends JpaRepository<UniversityMember, Long> {
    // 필요에 따라 커스텀 메서드 추가 가능
    // University 엔티티로 모든 부원 조회
    List<UniversityMember> findAllByUniversity(University university);

    Optional<UniversityMember> findByIdAndUniversity(Long memberId, University university);
}

