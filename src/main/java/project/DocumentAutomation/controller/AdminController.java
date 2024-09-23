package project.DocumentAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.DocumentAutomation.Service.UserService;
import project.DocumentAutomation.domain.User;
import project.DocumentAutomation.dto.CreateUniversityDto;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/createUniversity")
    public ResponseEntity<?> createUniversity(@RequestBody CreateUniversityDto createUniversityDto) {
        User university = userService.createUser(createUniversityDto.getUsername(),
                createUniversityDto.getPassword(),
                User.Role.UNIVERSITY);
        return ResponseEntity.ok("University created: " + university.getUsername());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        // 대시보드 데이터를 JSON 형태로 반환
        return ResponseEntity.ok("Admin Dashboard Data");
    }
}