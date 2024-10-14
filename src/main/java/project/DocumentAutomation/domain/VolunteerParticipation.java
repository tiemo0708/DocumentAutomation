package project.DocumentAutomation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//중간 매핑 엔티티
@Entity
@Table(name = "volunteer_participation")
@Getter
@NoArgsConstructor
public class VolunteerParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private VolunteerReport volunteerReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private UniversityMember universityMember;

    @Builder
    public VolunteerParticipation(VolunteerReport volunteerReport, UniversityMember universityMember) {
        this.volunteerReport = volunteerReport;
        this.universityMember = universityMember;
    }
}
