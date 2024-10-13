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
    private Boolean waiverSubmitted; //면책증명서 제출여부
    private Boolean consentSubmitted; //개인정보 동의서 제출 여부
}