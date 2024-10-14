package project.DocumentAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DocumentAutomation.dto.VolunteerReportDto;
import project.DocumentAutomation.service.VolunteerReportService;

import java.util.List;

@RestController
@RequestMapping("/api/universities/{universityName}/volunteer/reports")
@RequiredArgsConstructor
public class VolunteerReportController {

    private final VolunteerReportService volunteerReportService;

    @PostMapping("/{planId}")
    public ResponseEntity<Long> createVolunteerReport(@PathVariable Long planId,
                                                      @RequestBody VolunteerReportDto reportDto) {
        Long reportId = volunteerReportService.createVolunteerReport(planId, reportDto);
        return ResponseEntity.ok(reportId);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<VolunteerReportDto> getVolunteerReport(@PathVariable Long reportId) {
        VolunteerReportDto reportDto = volunteerReportService.getVolunteerReport(reportId);
        return ResponseEntity.ok(reportDto);
    }

    @GetMapping
    public ResponseEntity<List<VolunteerReportDto>> getAllVolunteerReports() {
        List<VolunteerReportDto> reportDtos = volunteerReportService.getAllVolunteerReports();
        return ResponseEntity.ok(reportDtos);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<Void> updateVolunteerReport(@PathVariable Long reportId,
                                                      @RequestBody VolunteerReportDto reportDto) {
        volunteerReportService.updateVolunteerReport(reportId, reportDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteVolunteerReport(@PathVariable Long reportId) {
        volunteerReportService.deleteVolunteerReport(reportId);
        return ResponseEntity.ok().build();
    }
}
