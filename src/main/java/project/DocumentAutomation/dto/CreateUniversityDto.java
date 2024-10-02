package project.DocumentAutomation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUniversityDto {
    private String universityName;
    private String clubName;
    private String establishedYear;
    private String password;

    public String getUsername() {
        return universityName;
    }
}
