package project.DocumentAutomation.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerReportDto {
    private Long planId = 0L;  // 계획서 ID (기본 값 설정)
    private String activityContent;  // 활동 내용 추가 (기본 값 설정)
    private String activitySummary ;  // 활동 소감 (기본 값 설정)
    private String activityLocation;  // 활동 장소 추가 (기본 값 설정)
    private LocalDate activityDate;  // 활동 날짜 추가 (기본 값 설정)
    private String purpose;  // 활동 목적 추가
    private List<String> photoUrls;  // 사진 리스트
    private List<Long> participants;  // 참가자 ID 리스트
    private String activityTime;  // 활동 시간 입력
}
