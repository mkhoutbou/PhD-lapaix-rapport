package com.lapaix.report.repositories;

import com.lapaix.report.domain.entities.ElementResultEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementResultRepository extends JpaRepository<ElementResultEntity, Long>, JpaSpecificationExecutor<ElementResultEntity> {

    List<ElementResultEntity> findElementResultsByReportId(Long reportId);
    // Define custom query methods if needed
}