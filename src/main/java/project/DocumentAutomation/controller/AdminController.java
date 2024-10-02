package project.DocumentAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.DocumentAutomation.Repository.UniversityRepository;
import project.DocumentAutomation.Repository.UserRepository;
import project.DocumentAutomation.Service.UserService;
import project.DocumentAutomation.domain.University;
import project.DocumentAutomation.domain.User;
import project.DocumentAutomation.dto.CreateUniversityDto;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final UserService userService;
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/createUniversity")
    public ResponseEntity<?> createUniversity(@RequestBody CreateUniversityDto createUniversityDto, Authentication authentication) {
        // 인증된 사용자 정보 가져오기 (Admin 사용자)
        String adminUsername = authentication.getName();
        User adminUser = userService.findByUsername(adminUsername)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        // 대학 계정을 위한 사용자 생성 (UNIVERSITY 역할 부여)
        User universityUser = User.builder()
                .username(createUniversityDto.getUsername())  // 새로운 대학 사용자 이름
                .password(passwordEncoder.encode(createUniversityDto.getPassword()))  // 비밀번호 암호화
                .role(User.Role.UNIVERSITY)  // 대학 사용자 역할 설정
                .build();

        // 대학 사용자 저장
        userRepository.save(universityUser);

        // University 객체 생성 (새로 생성된 대학 사용자와 연관)
        University university = University.builder()
                .universityName(createUniversityDto.getUniversityName())
                .clubName(createUniversityDto.getClubName())
                .establishedYear(createUniversityDto.getEstablishedYear())
                .password(passwordEncoder.encode(createUniversityDto.getPassword()))  // 대학 비밀번호 암호화
                .user(universityUser)  // 대학 사용자와 연관
                .build();

        // 대학 정보 저장
        universityRepository.save(university);

        // 연관된 User에 University 객체 설정 (대학 정보와 연관된 사용자)
        universityUser.setUniversity(university);
        userRepository.save(universityUser);  // 사용자 정보 업데이트

        return ResponseEntity.ok("University created for user: " + universityUser.getUsername());
    }



    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        // 대시보드 데이터를 JSON 형태로 반환
        return ResponseEntity.ok("Admin Dashboard Data");
    }
}