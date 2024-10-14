package project.DocumentAutomation.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.DocumentAutomation.domain.University;
import project.DocumentAutomation.domain.UniversityMember;
import project.DocumentAutomation.dto.UniversityMemberDto;
import project.DocumentAutomation.repository.UniversityMemberRepository;
import project.DocumentAutomation.repository.UniversityRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityMemberService {

    private final UniversityRepository universityRepository;
    private final UniversityMemberRepository universityMemberRepository;

    @Transactional
    public void addMemberToUniversity(String universityName, UniversityMemberDto memberDto) {
        // universityName으로 대학교 찾기
        University university = universityRepository.findById(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found: " + universityName));

        // 부원 추가 및 저장
        UniversityMember member = UniversityMember.builder()
                .name(memberDto.getName())
                .birthDate(memberDto.getBirthDate())
                .phoneNumber(memberDto.getPhoneNumber())
                .email(memberDto.getEmail())
                .gender(memberDto.getGender())
                .habitatId(memberDto.getHabitatId())
                .waiverSubmitted(memberDto.getWaiverSubmitted())
                .consentSubmitted(memberDto.getConsentSubmitted())
                .university(university)
                .build();

        universityMemberRepository.save(member);
    }

    @Transactional
    public void addMembersFromExcel(String universityName, MultipartFile file) throws IOException {
        // 엑셀 파일을 읽기 위한 Apache POI 사용
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Header row 건너뛰기
                }

                String name = getCellStringValue(row.getCell(0));
                String birthDate = getCellStringValue(row.getCell(1));
                String phoneNumber = getCellStringValue(row.getCell(2));
                String email = getCellStringValue(row.getCell(3));
                String gender = getCellStringValue(row.getCell(4));
                String habitatId = getCellStringValue(row.getCell(5));
                Boolean waiverSubmitted = Boolean.valueOf(getCellStringValue(row.getCell(6)));
                Boolean consentSubmitted = Boolean.valueOf(getCellStringValue(row.getCell(7)));

                // 대학교 조회 후 부원 추가
                University university = universityRepository.findById(universityName)
                        .orElseThrow(() -> new IllegalArgumentException("해당 대학교를 찾을 수 없습니다: " + universityName));

                UniversityMember member = UniversityMember.builder()
                        .name(name)
                        .birthDate(birthDate)
                        .phoneNumber(phoneNumber)
                        .email(email)
                        .gender(gender)
                        .habitatId(habitatId)
                        .waiverSubmitted(waiverSubmitted)
                        .consentSubmitted(consentSubmitted)
                        .university(university)
                        .build();

                universityMemberRepository.save(member);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<UniversityMemberDto> getAllMembers(String universityName) {
        University university = universityRepository.findById(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found: " + universityName));

        return universityMemberRepository.findAllByUniversity(university).stream()
                .map(member -> UniversityMemberDto.builder()
                        .name(member.getName())
                        .birthDate(member.getBirthDate())
                        .phoneNumber(member.getPhoneNumber())
                        .email(member.getEmail())
                        .gender(member.getGender())
                        .habitatId(member.getHabitatId())
                        .waiverSubmitted(member.getWaiverSubmitted())
                        .consentSubmitted(member.getConsentSubmitted())
                        .build())
                .collect(Collectors.toList());
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    @Transactional
    public void deleteMember(String universityName, Long memberId) {
        // universityName으로 대학교 찾기
        University university = universityRepository.findById(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found: " + universityName));

        // 멤버가 해당 대학교에 속해 있는지 확인 후 삭제
        UniversityMember member = universityMemberRepository.findByIdAndUniversity(memberId, university)
                .orElseThrow(() -> new IllegalArgumentException("Member not found for the given university"));

        universityMemberRepository.delete(member);
    }
}
