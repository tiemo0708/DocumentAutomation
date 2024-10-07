package project.DocumentAutomation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityMemberDto {
    private String name;
    private String birthDate;
    private String phoneNumber;
    private String email;
    private String gender;
    private String habitatId;
    private Boolean waiverSubmitted;
    private Boolean consentSubmitted;
}