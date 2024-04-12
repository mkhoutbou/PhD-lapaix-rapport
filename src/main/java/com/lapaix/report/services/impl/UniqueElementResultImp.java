package com.lapaix.report.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lapaix.report.domain.entities.UniqueElementResultsEntity;
import com.lapaix.report.repositories.UniqueElementResultsRepository;
import com.lapaix.report.services.UniqueElementResultsService;

@Service
public class UniqueElementResultImp implements UniqueElementResultsService {

    @Autowired
    private UniqueElementResultsRepository uniqueElementResultsRepository;


    @Override
    public Optional<UniqueElementResultsEntity> getUniqueElementResultById(Long id) {
        return uniqueElementResultsRepository.findById(id);
    }

    @Override
    public Page<UniqueElementResultsEntity> getAllUniqueElementResults(String pattern, Pageable pageable) {
        if(pattern != null && !pattern.isEmpty()) {
            Specification<UniqueElementResultsEntity> spec = Specification.where(null);
            spec = spec.or((root, query, cb) -> cb.like(root.get("description"), "%" + pattern + "%"));
            return uniqueElementResultsRepository.findAll(spec, pageable);
        }
        return uniqueElementResultsRepository.findAll(pageable);
    }

    @Override
    public UniqueElementResultsEntity createUniqueElementResult(UniqueElementResultsEntity uniqueElementResult) {
        return uniqueElementResultsRepository.save(uniqueElementResult);
    }

    @Override
    public UniqueElementResultsEntity updateUniqueElementResult(Long id,
            UniqueElementResultsEntity uniqueElementResult) {
        UniqueElementResultsEntity uniqueElementResultToUpdate = uniqueElementResultsRepository.findById(id).get();
        updateUniqueElementResultsFields(uniqueElementResult, uniqueElementResultToUpdate);
        return uniqueElementResultsRepository.save(uniqueElementResultToUpdate);
    }

    @Override
    public void deleteUniqueElementResult(Long id) {
       uniqueElementResultsRepository.deleteById(id);
    }

    @Override
    public Long count(String pattern) {
        if(pattern != null && !pattern.isEmpty()) {
            Specification<UniqueElementResultsEntity> spec = Specification.where(null);
            spec = spec.or((root, query, cb) -> cb.like(root.get("description"), "%" + pattern + "%"));
            return uniqueElementResultsRepository.count(spec);
        }
        return uniqueElementResultsRepository.count();
    }

    private void updateUniqueElementResultsFields(UniqueElementResultsEntity uniqueElementResult,
            UniqueElementResultsEntity uniqueElementResultToUpdate) {
        Optional.ofNullable(uniqueElementResult.getDescription()).ifPresent(uniqueElementResultToUpdate::setDescription);
    }
    
}
