package project.DocumentAutomation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volunteer_report")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private VolunteerPlan volunteerPlan;  // 계획서와의 연관 관계 설정 (일대일)

    @Column(nullable = false)
    private String activityContent;  // 활동 내용

    @Column(nullable = false)
    private String activitySummary;  // 활동 소감

    @Column(nullable = false)
    private String activityLocation;  // 활동 장소

    @Column(nullable = false)
    private LocalDate activityDate;  // 활동 날짜

    @Column(nullable = true)
    private String purpose;  // 활동 목적

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "volunteer_report_photos", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls;  // 사진 URL 리스트

    @OneToMany(mappedBy = "volunteerReport", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VolunteerParticipation> participants = new ArrayList<>();  // 참가 부원 목록 (중간 매핑 엔티티 이용)

    @Column(nullable = false)
    private String activityTime;  // 활동 시간 입력 (HH:MM-HH:MM 형식)

    public void updateReport(String activitySummary, List<String> photoUrls,
                             List<VolunteerParticipation> participants, String activityTime) {
        this.activitySummary = activitySummary;
        this.photoUrls = photoUrls != null ? photoUrls : this.photoUrls;
        if (participants != null) {
            this.participants.clear();
            this.participants.addAll(participants);
        }
        this.activityTime = activityTime;
    }
}