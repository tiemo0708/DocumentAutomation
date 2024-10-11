package project.DocumentAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DocumentAutomation.dto.VolunteerPlanDto;
import project.DocumentAutomation.service.VolunteerPlanService;

import java.util.List;

@RestController
@RequestMapping("/api/universities/{universityName}/volunteer")
@RequiredArgsConstructor
public class VolunteerPlanController {

    private final VolunteerPlanService volunteerPlanService;

    @PostMapping
    public ResponseEntity<Void> createVolunteerPlan(@PathVariable String universityName, @RequestBody VolunteerPlanDto volunteerPlanDto) {
        volunteerPlanService.createVolunteerPlan(universityName, volunteerPlanDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{planId}")
    public ResponseEntity<VolunteerPlanDto> getVolunteerPlan(@PathVariable String universityName, @PathVariable Long planId) {
        VolunteerPlanDto volunteerPlanDto = volunteerPlanService.getVolunteerPlan(universityName, planId);
        return ResponseEntity.ok(volunteerPlanDto);
    }

    @GetMapping
    public ResponseEntity<List<VolunteerPlanDto>> getAllVolunteerPlans(@PathVariable String universityName) {
        List<VolunteerPlanDto> volunteerPlans = volunteerPlanService.getAllVolunteerPlans(universityName);
        return ResponseEntity.ok(volunteerPlans);
    }
}
