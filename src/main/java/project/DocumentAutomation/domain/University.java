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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String universityName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String establishedYear;

    @Builder
    public University(String universityName, String location, String establishedYear) {
        this.universityName = universityName;
        this.location = location;
        this.establishedYear = establishedYear;
    }
}