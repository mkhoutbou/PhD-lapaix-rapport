package com.lapaix.report.configs.sequence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepository extends JpaRepository<SequenceConfig, Long>{
}


