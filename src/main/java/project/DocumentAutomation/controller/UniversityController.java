package project.DocumentAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DocumentAutomation.dto.UniversityMemberDto;
import project.DocumentAutomation.service.UniversityService;


@RestController
@RequestMapping("/api/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @PostMapping("/{universityName}/members")
    public ResponseEntity<Void> addMemberToUniversity(@PathVariable String universityName, @RequestBody UniversityMemberDto memberDto) {
        universityService.addMemberToUniversity(universityName, memberDto);
        return ResponseEntity.ok().build();
    }
}