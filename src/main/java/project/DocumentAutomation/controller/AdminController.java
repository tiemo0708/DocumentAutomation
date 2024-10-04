package project.DocumentAutomation.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class AdminController {

    private final UserService userService;
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/createUniversity")
    @Transactional // 트랜잭션 추가
    public ResponseEntity<?> createUniversity(@RequestBody CreateUniversityDto createUniversityDto, Authentication authentication) {
        // 인증된 사용자 정보 가져오기 (Admin 사용자)
        String adminUsername = authentication.getName();
        User adminUser = userService.findByUsername(adminUsername)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        // 동일한 사용자 이름이 존재하는지 확인
        if (userRepository.findByUsername(createUniversityDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists.");
        }

        // 동일한 대학 이름이 존재하는지 확인
        if (universityRepository.findByUniversityName(createUniversityDto.getUniversityName()).isPresent()) {
            return ResponseEntity.badRequest().body("University name already exists.");
        }

        // 대학 객체 먼저 생성 및 저장
        University university = University.builder()
                .universityName(createUniversityDto.getUniversityName())
                .clubName(createUniversityDto.getClubName())
                .establishedYear(createUniversityDto.getEstablishedYear())
                .password(passwordEncoder.encode(createUniversityDto.getPassword())) // 비밀번호 암호화
                .build();

        universityRepository.save(university); // 먼저 University 저장

        // 대학 계정을 위한 사용자 생성
        User universityUser = User.builder()
                .username(createUniversityDto.getUsername()) // 새로운 대학 사용자 이름
                .password(passwordEncoder.encode(createUniversityDto.getPassword())) // 비밀번호 암호화
                .role(User.Role.UNIVERSITY) // 대학 사용자 역할 설정
                .university(university) // University와 연관 설정
                .build();

        // 대학 사용자 저장
        userRepository.save(universityUser);

        return ResponseEntity.ok("University created for user: " + universityUser.getUsername());
    }


    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        // 대시보드 데이터를 JSON 형태로 반환
        return ResponseEntity.ok("Admin Dashboard Data");
    }
}
