package project.DocumentAutomation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "universities")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class University {

    @Id
    @Column(name = "university_name")
    private String universityName;

    @Column(nullable = false)
    private String clubName;

    @Column(nullable = false)
    private String establishedYear;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "university", fetch = FetchType.LAZY) // User에서 university가 주인임을 명시
    private User user;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniversityMember> universityMembers = new ArrayList<>();

    public void addMember(String name, String birthDate, String phoneNumber, String email, String gender,
                          String habitatId, Boolean waiverSubmitted, Boolean consentSubmitted) {
        // 새로운 UniversityMember 객체 생성 시, university 필드를 직접 설정
        UniversityMember universityMember = UniversityMember.builder()
                .name(name)
                .birthDate(birthDate)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(gender)
                .habitatId(habitatId)
                .waiverSubmitted(waiverSubmitted)
                .consentSubmitted(consentSubmitted)
                .university(this)  // university 필드를 직접 설정
                .build();

        this.universityMembers.add(universityMember);
    }

    @Builder
    public University(String universityName, String clubName, String establishedYear, String password, User user) {
        this.universityName = universityName;
        this.clubName = clubName;
        this.establishedYear = establishedYear;
        this.password = password;
        this.user = user; // 연관된 사용자 설정
    }
}
