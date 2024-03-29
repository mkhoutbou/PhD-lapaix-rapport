package com.lapaix.report.services.impl;

import com.lapaix.report.domain.entities.ElementResultEntity;
import com.lapaix.report.domain.entities.ReportEntity;
import com.lapaix.report.repositories.ElementResultRepository;
import com.lapaix.report.repositories.ReportRepository;
import com.lapaix.report.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ElementResultRepository elementResultRepository;

    @Override
    public Optional<ReportEntity> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    public Page<ReportEntity> getAllReports(String pattern , @NonNull Pageable pageable) {

        if (pattern != null && !pattern.isEmpty()) {
            Specification<ReportEntity> spec = Specification.where(null);
            spec = spec.or((root, query, cb) -> cb.like(root.get("codeReport"), "%" + pattern + "%"))
                    .or((root, query, cb) -> cb.like(root.get("medecin"), "%" + pattern + "%"))
                    .or((root, query, cb) -> cb.like(root.get("titre"), "%" + pattern + "%"))
                    .or((root, query, cb) -> cb.like(root.get("conclusion"), "%" + pattern + "%"));
    
            return reportRepository.findAll(spec, pageable);
        }
        return reportRepository.findAll(pageable);
    }

    @Override
    public ReportEntity createReport(ReportEntity report) {
        return reportRepository.save(report);
    }

    @Override
    public ReportEntity updateReport(Long id, ReportEntity report) {
        ReportEntity reportToUpdate = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        updateReportFields(report, reportToUpdate);
        return reportRepository.save(reportToUpdate);
    }

    @Override
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    @Override
    public Long count(String pattern) {

        if (pattern != null && !pattern.isBlank()) {
            Specification<ReportEntity> spec = Specification.where(null);
            spec = spec.or((root, query, cb) -> cb.like(root.get("codeReport"), "%" + pattern + "%"))
                    .or((root, query, cb) -> cb.like(root.get("medecin"), "%" + pattern + "%"))
                    .or((root, query, cb) -> cb.like(root.get("titre"), "%" + pattern + "%"))
                    .or((root, query, cb) -> cb.like(root.get("conclusion"), "%" + pattern + "%"));
    
            return reportRepository.count(spec);
        }
        return reportRepository.count();
    }

    @Override
    public List<ElementResultEntity> getReportElementResults(Long reportId) {
        
        return elementResultRepository.findElementResultsByReportId(reportId);
    }

    private void updateReportFields(ReportEntity report, ReportEntity reportToUpdate) {
        Optional.ofNullable(report.getBulletinId()).ifPresent(reportToUpdate::setBulletinId);
        Optional.ofNullable(report.getCodeReport()).ifPresent(reportToUpdate::setCodeReport);
        Optional.ofNullable(report.getDate()).ifPresent(reportToUpdate::setDate);
        Optional.ofNullable(report.getTitre()).ifPresent(reportToUpdate::setTitre);
        Optional.ofNullable(report.getMedecin()).ifPresent(reportToUpdate::setMedecin);
        Optional.ofNullable(report.getConclusion()).ifPresent(reportToUpdate::setConclusion);
    }
}
