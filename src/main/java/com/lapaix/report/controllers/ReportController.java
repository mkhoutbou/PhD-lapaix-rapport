package com.lapaix.report.controllers;

import com.lapaix.report.domain.dtos.ElementResultDTO;
import com.lapaix.report.domain.dtos.ReportDTO;
import com.lapaix.report.domain.entities.ReportEntity;
import com.lapaix.report.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/count")
    public ResponseEntity<Long> count(
            @RequestParam(value = "search_string", defaultValue = "") String searchString) {

        long count = reportService.count(searchString);
        return ResponseEntity.ok(count);
    }

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search_string", defaultValue = "") String searchString) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("titre").descending());

        Page<ReportEntity> reportPage = reportService.getAllReports(searchString, pageable);
        List<ReportDTO> reportDTOs = ReportDTO.toDTOs(reportPage.getContent());
        return ResponseEntity.ok(reportDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        Optional<ReportEntity> report = reportService.getReportById(id);
        return report.map(entity -> ResponseEntity.ok(ReportDTO.toDTO(entity)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReportDTO> createReport(@RequestBody ReportDTO reportDTO) {
        ReportEntity report = reportService.createReport(reportDTO.toEntity());
        return ResponseEntity.ok(ReportDTO.toDTO(report));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateReport(
            @PathVariable Long id, @RequestBody ReportDTO reportDTO) {
        ReportDTO updatedReportDTO = ReportDTO.toDTO(
                reportService.updateReport(id, reportDTO.toEntity()));
        return ResponseEntity.ok(updatedReportDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }

    @GetMapping("/{reportId}/elementResults")
    public ResponseEntity<List<ElementResultDTO>> getReportElementResults(@PathVariable Long reportId) {
        Optional<ReportEntity> report = reportService.getReportById(reportId);
        if (report.isPresent()) {
            List<ElementResultDTO> elementResultDTOs = ElementResultDTO.toDTOs(report.get().getResults());
            return ResponseEntity.ok(elementResultDTOs);
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }

}
