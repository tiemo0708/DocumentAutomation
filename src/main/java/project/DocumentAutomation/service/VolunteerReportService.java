package project.DocumentAutomation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DocumentAutomation.domain.UniversityMember;
import project.DocumentAutomation.domain.VolunteerParticipation;
import project.DocumentAutomation.domain.VolunteerPlan;
import project.DocumentAutomation.domain.VolunteerReport;
import project.DocumentAutomation.dto.VolunteerReportDto;
import project.DocumentAutomation.repository.UniversityMemberRepository;
import project.DocumentAutomation.repository.VolunteerPlanRepository;
import project.DocumentAutomation.repository.VolunteerReportRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerReportService {

    private final VolunteerReportRepository volunteerReportRepository;
    private final VolunteerPlanRepository volunteerPlanRepository;
    private final UniversityMemberRepository universityMemberRepository;

    @Transactional
    public Long createVolunteerReport(Long planId, VolunteerReportDto reportDto) {
        // VolunteerPlan 조회
        VolunteerPlan volunteerPlan = volunteerPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer Plan not found with id: " + planId));

        // VolunteerReport 생성
        // 봉사 보고서 생성
        VolunteerReport volunteerReport = VolunteerReport.builder()
                .volunteerPlan(volunteerPlan)
                .activityContent(volunteerPlan.getActivityContent() != null ? volunteerPlan.getActivityContent() : "기본 활동 내용")
                .activitySummary(reportDto.getActivitySummary())
                .activityLocation(volunteerPlan.getActivityLocation() != null ? volunteerPlan.getActivityLocation() : "기본 활동 장소")
                .activityDate(volunteerPlan.getActivityDate() != null ? volunteerPlan.getActivityDate() : LocalDate.now())
                .purpose(volunteerPlan.getPurpose() != null ? volunteerPlan.getPurpose() : "기본 활동 목적")
                .photoUrls(reportDto.getPhotoUrls())
                .activityTime(reportDto.getActivityTime())
                .participants(new ArrayList<>()) // participants 리스트 초기화
                .build();

// Participants 추가
        List<VolunteerParticipation> participations = reportDto.getParticipants().stream()
                .map(memberId -> {
                    UniversityMember member = universityMemberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("University Member not found with id: " + memberId));
                    return VolunteerParticipation.builder()
                            .volunteerReport(volunteerReport)
                            .universityMember(member)
                            .build();
                }).collect(Collectors.toList());

        volunteerReport.getParticipants().addAll(participations);

// 최종 저장
        return volunteerReportRepository.save(volunteerReport).getId();

    }
        @Transactional(readOnly = true)
    public VolunteerReportDto getVolunteerReport(Long reportId) {
        VolunteerReport volunteerReport = volunteerReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer Report not found with id: " + reportId));

        return convertToDto(volunteerReport);
    }

    @Transactional(readOnly = true)
    public List<VolunteerReportDto> getAllVolunteerReports() {
        return volunteerReportRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateVolunteerReport(Long reportId, VolunteerReportDto reportDto) {
        VolunteerReport existingReport = volunteerReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer Report not found with id: " + reportId));

        // 기존 참여자 제거
        existingReport.getParticipants().clear();

        // Participants 추가
        List<VolunteerParticipation> participations = reportDto.getParticipants().stream()
                .map(memberId -> {
                    UniversityMember member = universityMemberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("University Member not found with id: " + memberId));
                    return VolunteerParticipation.builder()
                            .volunteerReport(existingReport)
                            .universityMember(member)
                            .build();
                }).collect(Collectors.toList());

        existingReport.getParticipants().addAll(participations);

        // 기존 객체 수정
        existingReport.updateReport(
                reportDto.getActivitySummary(),
                reportDto.getPhotoUrls(),
                participations,
                reportDto.getActivityTime()
        );

        // VolunteerReportRepository에 저장
        volunteerReportRepository.save(existingReport);
    }

    @Transactional
    public void deleteVolunteerReport(Long reportId) {
        if (!volunteerReportRepository.existsById(reportId)) {
            throw new IllegalArgumentException("Volunteer Report not found with id: " + reportId);
        }
        volunteerReportRepository.deleteById(reportId);
    }

    private VolunteerReportDto convertToDto(VolunteerReport volunteerReport) {
        return VolunteerReportDto.builder()
                .planId(volunteerReport.getVolunteerPlan().getId())
                .activityContent(volunteerReport.getActivityContent())
                .activitySummary(volunteerReport.getActivitySummary())
                .activityLocation(volunteerReport.getActivityLocation())
                .activityDate(volunteerReport.getActivityDate())
                .purpose(volunteerReport.getPurpose())
                .photoUrls(volunteerReport.getPhotoUrls())
                .participants(volunteerReport.getParticipants().stream()
                        .map(participation -> participation.getUniversityMember().getId())
                        .collect(Collectors.toList()))
                .activityTime(volunteerReport.getActivityTime())
                .build();
    }
}
