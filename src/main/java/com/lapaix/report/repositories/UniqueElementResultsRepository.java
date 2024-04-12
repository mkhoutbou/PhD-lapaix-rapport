package com.lapaix.report.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lapaix.report.domain.entities.UniqueElementResultsEntity;

@Repository
public interface UniqueElementResultsRepository extends JpaRepository<UniqueElementResultsEntity, Long>, JpaSpecificationExecutor<UniqueElementResultsEntity>{
    
}
