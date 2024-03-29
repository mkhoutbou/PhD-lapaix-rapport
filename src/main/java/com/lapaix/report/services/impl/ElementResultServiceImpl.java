package com.lapaix.report.services.impl;

import com.lapaix.report.domain.entities.ElementResultEntity;
import com.lapaix.report.domain.entities.ReportEntity;
import com.lapaix.report.repositories.ElementResultRepository;
import com.lapaix.report.repositories.ReportRepository;
import com.lapaix.report.services.ElementResultService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import java.util.Optional;

@Service
public class ElementResultServiceImpl implements ElementResultService {

    @Autowired
    private ElementResultRepository elementResultRepository;

    @Autowired
    private ReportRepository reportRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ElementResultEntity> getElementResultById(Long id) {
        return elementResultRepository.findById(id);
    }

    @Override
    public Page<ElementResultEntity> getAllElementResults(String pattern, @NonNull Pageable pageable) {
        if(pattern != null && !pattern.isEmpty()) {
            Specification<ElementResultEntity> spec = Specification.where(null);
            spec = spec.or((root, query, cb) -> cb.like(root.get("description"), "%" + pattern + "%"));
            if (pattern.matches("\\d+")) {
                spec = spec.or((root, query, cb) -> cb.equal(root.get("id"), Long.parseLong(pattern)));
            }
            if (pattern.equalsIgnoreCase("true") || pattern.equalsIgnoreCase("false")) {
                spec = spec.or((root, query, cb) -> cb.equal(root.get("resultatGeneral"), Boolean.parseBoolean(pattern)));
                
            }

            return elementResultRepository.findAll(spec, pageable);
        }
        return elementResultRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public ElementResultEntity createElementResult(ElementResultEntity elementResult) {
        
        if(elementResult.getReport() != null && elementResult.getReport().getId() != null) {
            ReportEntity report = reportRepository.findById(elementResult.getReport().getId())
                    .orElseThrow(() -> new RuntimeException("Report not found"));
            entityManager.merge(report);
            elementResult.setReport(report);
        }
        
        return elementResultRepository.save(elementResult);
    }

    @Override
    public ElementResultEntity updateElementResult(Long id, ElementResultEntity elementResult) {
        ElementResultEntity elementResultToUpdate = elementResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ElementResult not found"));
        updateElementResultFields(elementResult, elementResultToUpdate);
        return elementResultRepository.save(elementResultToUpdate);
    }

    @Override
    public void deleteElementResult(Long id) {
        elementResultRepository.deleteById(id);
    }

    @Override
    public Long count(String pattern) {
        if(pattern != null && !pattern.isEmpty()) {
            Specification<ElementResultEntity> spec = Specification.where(null);
            spec = spec.or((root, query, cb) -> cb.like(root.get("description"), "%" + pattern + "%"));
            if (pattern.matches("\\d+")) {
                spec = spec.or((root, query, cb) -> cb.equal(root.get("id"), Long.parseLong(pattern)));
            }
            if (pattern.equalsIgnoreCase("true") || pattern.equalsIgnoreCase("false")) {
                spec = spec.or((root, query, cb) -> cb.equal(root.get("resultatGeneral"), Boolean.parseBoolean(pattern)));
                
            }

            return elementResultRepository.count(spec);
        }
        return elementResultRepository.count();
    }

    private void updateElementResultFields(ElementResultEntity elementResult,
            ElementResultEntity elementResultToUpdate) {
        Optional.ofNullable(elementResult.isResultatGeneral()).ifPresent(elementResultToUpdate::setResultatGeneral);
        Optional.ofNullable(elementResult.getDescription()).ifPresent(elementResultToUpdate::setDescription);
    }
}
