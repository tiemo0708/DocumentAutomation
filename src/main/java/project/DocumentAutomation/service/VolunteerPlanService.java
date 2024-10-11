package project.DocumentAutomation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DocumentAutomation.domain.University;
import project.DocumentAutomation.domain.VolunteerPlan;
import project.DocumentAutomation.dto.VolunteerPlanDto;
import project.DocumentAutomation.repository.UniversityRepository;
import project.DocumentAutomation.repository.VolunteerPlanRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerPlanService {

    private final UniversityRepository universityRepository;
    private final VolunteerPlanRepository volunteerPlanRepository;

    @Transactional
    public void createVolunteerPlan(String universityName, VolunteerPlanDto volunteerPlanDto) {
        // universityName으로 대학교 찾기
        University university = universityRepository.findById(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found: " + universityName));

        // VolunteerPlan 엔티티 생성 및 대학교 연관 설정
        VolunteerPlan volunteerPlan = VolunteerPlan.builder()
                .university(university)
                .clubName(university.getClubName())
                .submitterName(volunteerPlanDto.getSubmitterName())
                .activityDate(volunteerPlanDto.getActivityDate())
                .expectedParticipants(volunteerPlanDto.getExpectedParticipants())
                .activityType(volunteerPlanDto.getActivityType())
                .activityLocation(volunteerPlanDto.getActivityLocation())
                .purpose(volunteerPlanDto.getPurpose())
                .activityContent(volunteerPlanDto.getActivityContent())
                .additionalRequest(volunteerPlanDto.getAdditionalRequest())
                .submitDate(LocalDate.now()) // 제출일을 현재 날짜로 설정
                .build();

        // 봉사 계획서 저장
        volunteerPlanRepository.save(volunteerPlan);
    }

    @Transactional(readOnly = true)
    public VolunteerPlanDto getVolunteerPlan(String universityName, Long planId) {
        // universityName과 planId로 봉사 계획서 조회
        VolunteerPlan volunteerPlan = volunteerPlanRepository.findByIdAndUniversity_UniversityName(planId, universityName)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer plan not found for the given university"));

        // VolunteerPlan 엔티티를 DTO로 변환
        return convertToDto(volunteerPlan);
    }

    @Transactional(readOnly = true)
    public List<VolunteerPlanDto> getAllVolunteerPlans(String universityName) {
        // universityName으로 모든 봉사 계획서 조회
        List<VolunteerPlan> volunteerPlans = volunteerPlanRepository.findAllByUniversity_UniversityName(universityName);

        // 봉사 계획서 리스트를 DTO 리스트로 변환
        return volunteerPlans.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private VolunteerPlanDto convertToDto(VolunteerPlan volunteerPlan) {
        return VolunteerPlanDto.builder()
                .universityName(volunteerPlan.getUniversity().getUniversityName())
                .clubName(volunteerPlan.getClubName())
                .submitterName(volunteerPlan.getSubmitterName())
                .activityDate(volunteerPlan.getActivityDate())
                .expectedParticipants(volunteerPlan.getExpectedParticipants())
                .activityType(volunteerPlan.getActivityType())
                .activityLocation(volunteerPlan.getActivityLocation())
                .purpose(volunteerPlan.getPurpose())
                .activityContent(volunteerPlan.getActivityContent())
                .additionalRequest(volunteerPlan.getAdditionalRequest())
                .build();
    }
}
