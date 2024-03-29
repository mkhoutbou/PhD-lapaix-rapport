package com.lapaix.report.services;

import com.lapaix.report.domain.entities.ElementResultEntity;
import com.lapaix.report.domain.entities.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReportService {

    Optional<ReportEntity> getReportById(Long id);
    Page<ReportEntity> getAllReports(String pattern , Pageable pageable);
    ReportEntity createReport(ReportEntity report);
    ReportEntity updateReport(Long id, ReportEntity report);
    void deleteReport(Long id);
    Long count(String patteString);
    List<ElementResultEntity> getReportElementResults(Long reportId);
}
