package project.DocumentAutomation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Setter
    @JsonIgnore  // JSON 직렬화 시 무한 루프 방지
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", unique = true) // 외래 키와 유니크 설정
    private University university;

    @Builder
    public User(String username, String password, Role role, University university) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.university = university;
    }

    public enum Role {
        ADMIN, UNIVERSITY
    }
}