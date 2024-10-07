package project.DocumentAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.DocumentAutomation.dto.UniversityMemberDto;
import project.DocumentAutomation.service.UniversityService;


@RestController
@RequestMapping("/api/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @PostMapping("/{universityName}/members")
    public ResponseEntity<String> addMemberToUniversity(@PathVariable String universityName, @RequestBody UniversityMemberDto memberDto) {
        universityService.addMemberToUniversity(universityName, memberDto);
        return ResponseEntity.ok("부원이 성공적으로 추가되었습니다.");
    }

    @PostMapping("/{universityName}/uploadMembers")
    public ResponseEntity<?> uploadUniversityMembers(@PathVariable String universityName,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            universityService.addMembersFromExcel(universityName, file);
            return ResponseEntity.ok("부원들이 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }
}