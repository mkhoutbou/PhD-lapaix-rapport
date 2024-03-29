package com.lapaix.report.services;

import com.lapaix.report.domain.entities.ElementResultEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ElementResultService {

    Optional<ElementResultEntity> getElementResultById(Long id);
    Page<ElementResultEntity> getAllElementResults(String pattern, Pageable pageable);
    ElementResultEntity createElementResult(ElementResultEntity elementResult);
    ElementResultEntity updateElementResult(Long id, ElementResultEntity elementResult);
    void deleteElementResult(Long id);
    Long count(String pattern);
}
