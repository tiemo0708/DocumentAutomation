package project.DocumentAutomation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "university_members")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UniversityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 8)
    private String birthDate;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 1)
    private String gender; // 'M' 또는 'F'로 저장

    @Column(nullable = false)
    private String habitatId;

    @Column(nullable = false)
    private Boolean waiverSubmitted;

    @Column(nullable = false)
    private Boolean consentSubmitted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Builder
    public UniversityMember(String name, String birthDate, String phoneNumber, String email, String gender,
                  String habitatId, Boolean waiverSubmitted, Boolean consentSubmitted, University university) {
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.habitatId = habitatId;
        this.waiverSubmitted = waiverSubmitted;
        this.consentSubmitted = consentSubmitted;
        this.university = university;
    }
}