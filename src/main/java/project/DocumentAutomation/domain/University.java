package project.DocumentAutomation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY) // 사용자와의 관계 설정
    @JoinColumn(name = "user_id") // 외래 키 설정
    private User user;

    @Builder
    public University(String universityName, String clubName, String establishedYear, String password, User user) {
        this.universityName = universityName;
        this.clubName = clubName;
        this.establishedYear = establishedYear;
        this.password = password;
        this.user = user; // 연관된 사용자 설정
    }
}
