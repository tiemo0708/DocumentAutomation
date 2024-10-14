package project.DocumentAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.DocumentAutomation.dto.UniversityMemberDto;
import project.DocumentAutomation.service.UniversityMemberService;


@RestController
@RequestMapping("/api/universities")
@RequiredArgsConstructor
public class UniversityMemberController {

    private final UniversityMemberService universityMemberService;

    @PostMapping("/{universityName}/members")
    public ResponseEntity<String> addMemberToUniversity(@PathVariable String universityName, @RequestBody UniversityMemberDto memberDto) {
        universityMemberService.addMemberToUniversity(universityName, memberDto);
        return ResponseEntity.ok("부원이 성공적으로 추가되었습니다.");
    }

    @PostMapping("/{universityName}/uploadMembers")
    public ResponseEntity<?> uploadUniversityMembers(@PathVariable String universityName,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            universityMemberService.addMembersFromExcel(universityName, file);
            return ResponseEntity.ok("부원들이 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }

    // 새로운 API: 특정 대학교의 부원 목록 조회
    @GetMapping("/{universityName}/members")
    public ResponseEntity<?> getAllMembers(@PathVariable String universityName) {
        try {
            return ResponseEntity.ok(universityMemberService.getAllMembers(universityName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("부원 목록 조회 실패: " + e.getMessage());
        }
    }

    // 새로운 API: 특정 부원 삭제
    @DeleteMapping("/{universityName}/members/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable String universityName, @PathVariable Long memberId) {
        try {
            universityMemberService.deleteMember(universityName, memberId);
            return ResponseEntity.ok("부원이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("부원 삭제 실패: " + e.getMessage());
        }
    }

}
