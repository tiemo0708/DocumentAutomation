package project.DocumentAutomation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class VolunteerPlanDto {
    private String universityName;         // 대학교 이름
    private String clubName;               // 동아리 이름
    private String submitterName;          // 제출자 이름
    private LocalDate activityDate;        // 활동 일자
    private int expectedParticipants;      // 예상 참가 인원
    private String activityType;           // 활동 구분 (예: 교육, 봉사)
    private String activityLocation;       // 활동 장소 (주소)
    private String purpose;                // 활동 목적
    private String activityContent;        // 활동 내용
    private String additionalRequest;      // 본부 요청사항
    private LocalDate submitDate;          // 제출일
}
