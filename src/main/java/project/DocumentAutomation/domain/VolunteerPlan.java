package project.DocumentAutomation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "volunteer_plan")
@Getter
@NoArgsConstructor
public class VolunteerPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Column(nullable = false)
    private String clubName;  // 동아리 이름

    @Column(nullable = false)
    private String submitterName;  // 제출자 이름

    @Column(nullable = false)
    private LocalDate activityDate;  // 활동일자

    @Column(nullable = false)
    private int expectedParticipants;  // 예상 참가 인원

    @Column(nullable = false)
    private String activityType;  // 활동 구분

    @Column(nullable = false)
    private String activityLocation;  // 활동장소 주소

    @Column(nullable = false)
    private String purpose;  // 활동 목적

    @Column(nullable = false)
    private String activityContent;  // 활동 내용

    @Column
    private String additionalRequest;  // 본부 요청사항

    @Column(nullable = false)
    private LocalDate submitDate;  // 제출일

    @Builder
    public VolunteerPlan(University university, String clubName, String submitterName, LocalDate activityDate,
                         int expectedParticipants, String activityType, String activityLocation, String purpose,
                         String activityContent, String additionalRequest, LocalDate submitDate) {
        this.university = university;
        this.clubName = clubName;
        this.submitterName = submitterName;
        this.activityDate = activityDate;
        this.expectedParticipants = expectedParticipants;
        this.activityType = activityType;
        this.activityLocation = activityLocation;
        this.purpose = purpose;
        this.activityContent = activityContent;
        this.additionalRequest = additionalRequest;
        this.submitDate = submitDate;
    }
}
