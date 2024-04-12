package com.lapaix.report.services;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.lapaix.report.domain.entities.UniqueElementResultsEntity;

public interface UniqueElementResultsService {
    
    Optional<UniqueElementResultsEntity> getUniqueElementResultById(Long id);
    org.springframework.data.domain.Page<UniqueElementResultsEntity> getAllUniqueElementResults(String pattern, Pageable pageable);
    UniqueElementResultsEntity createUniqueElementResult(UniqueElementResultsEntity uniqueElementResult);
    UniqueElementResultsEntity updateUniqueElementResult(Long id, UniqueElementResultsEntity uniqueElementResult);
    void deleteUniqueElementResult(Long id);
    Long count(String pattern);
}
