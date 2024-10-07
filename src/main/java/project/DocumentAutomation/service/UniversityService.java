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
import project.DocumentAutomation.dto.UniversityMemberDto;
import project.DocumentAutomation.repository.UniversityRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;

    @Transactional
    public void addMemberToUniversity(String universityName, UniversityMemberDto memberDto) {
        // universityName으로 대학교 찾기
        University university = universityRepository.findById(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found: " + universityName));

        // 부원 추가
        university.addMember(memberDto.getName(), memberDto.getBirthDate(), memberDto.getPhoneNumber(),
                memberDto.getEmail(), memberDto.getGender(), memberDto.getHabitatId(),
                memberDto.getWaiverSubmitted(), memberDto.getConsentSubmitted());
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

                university.addMember(name, birthDate, phoneNumber, email, gender, habitatId, waiverSubmitted, consentSubmitted);
            }
        }
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
}