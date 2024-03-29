package com.lapaix.report.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import com.lapaix.report.domain.entities.ElementResultEntity;
import com.lapaix.report.domain.entities.ReportEntity;
import com.lapaix.report.repositories.ElementResultRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ElementResultServiceTest {

    @Autowired
    private ElementResultService elementResultService;

    @Autowired
    private ElementResultRepository elementResultRepository;

    @Test
    void testCount() {

        // Given
        ElementResultEntity elementResultEntity1 = ElementResultEntity.builder()
                .resultatGeneral(true)
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-001")
                        .medecin("Dr. Test")
                        .build()
                )
                .description("Test Element Result 1")
                .build();

        ElementResultEntity elementResultEntity2 = ElementResultEntity.builder()
                .resultatGeneral(false)
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-002")
                        .medecin("Dr. Test")
                        .build()
                )
                .description("Test Element Result 2")
                .build();

        ElementResultEntity elementResultEntity3 = ElementResultEntity.builder()
                .resultatGeneral(true)
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-003")
                        .medecin("Dr. Test")
                        .build()
                )
                .description("Test Element Result 3")
                .build();

        // When
        elementResultRepository.save(elementResultEntity1);
        elementResultRepository.save(elementResultEntity2);
        elementResultRepository.save(elementResultEntity3);

        Long totalCount = elementResultService.count(null);

        // Then
        assertEquals(3, totalCount);

        // When querying with a specific criterion
        long filteredCount = elementResultService.count("True");

        // Then
        assertEquals(2, filteredCount);

    }

    @Test
    void testCreateElementResult() {

        // Given
        ElementResultEntity elementResultEntity = ElementResultEntity.builder()
                .resultatGeneral(true)
                .description("Test Element Result")
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-001")
                        .medecin("Dr. Test")
                        .build()
                )
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        // When
        ElementResultEntity createdElementResult = elementResultService.createElementResult(elementResultEntity);

        // Then
        assertNotNull(createdElementResult.getId());
        assertEquals(elementResultEntity.isResultatGeneral(), createdElementResult.isResultatGeneral());
        assertEquals(elementResultEntity.getDescription(), createdElementResult.getDescription());
        assertEquals(elementResultEntity.getCreatedDate(), createdElementResult.getCreatedDate());
        assertEquals(elementResultEntity.getLastModifiedDate(), createdElementResult.getLastModifiedDate());

    }

    @Test
    void testDeleteElementResult() {

        // Given
        ElementResultEntity elementResultEntity = ElementResultEntity.builder()
                .resultatGeneral(true)
                .description("Test Element Result")
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-001")
                        .medecin("Dr. Test")
                        .build()
                )
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ElementResultEntity createdElementResult = elementResultService.createElementResult(elementResultEntity);

        // When
        elementResultService.deleteElementResult(createdElementResult.getId());

        // Then
        assertEquals(0, elementResultService.count(null));

    }

    @Test
    void testGetAllElementResults() {

        // Given
        ElementResultEntity elementResultEntity1 = ElementResultEntity.builder()
                .resultatGeneral(true)
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-001")
                        .medecin("Dr. Test")
                        .build()
                )
                .description("Test Element Result 1")
                .build();
        ElementResultEntity elementResultEntity2 = ElementResultEntity.builder()
                .resultatGeneral(false)
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-002")
                        .medecin("Dr. Test")
                        .build()
                )
                .description("Test Element Result 2")
                .build();
        
        // When
        elementResultRepository.save(elementResultEntity1);
        elementResultRepository.save(elementResultEntity2);

        // Then
        Pageable pageable = PageRequest.of(0, 10);
        assertEquals(2, elementResultService.getAllElementResults(null, pageable).get().count());
        assertEquals(1, elementResultService.getAllElementResults("True", pageable).get().count());
    }

    @Test
    void testGetElementResultById() {

        // Given
        ElementResultEntity elementResultEntity = ElementResultEntity.builder()
                .resultatGeneral(true)
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-001")
                        .medecin("Dr. Test")
                        .build()
                )
                .description("Test Element Result")
                .build();

        ElementResultEntity createdElementResult = elementResultService.createElementResult(elementResultEntity);

        // When
        ElementResultEntity foundElementResult = elementResultService.getElementResultById(createdElementResult.getId()).get();

        // Then
        assertNotNull(foundElementResult);

    }

    @Test
    void testUpdateElementResult() {

        // Given
        ElementResultEntity elementResultEntity = ElementResultEntity.builder()
                .resultatGeneral(true)
                .report(
                        ReportEntity.builder()
                        .codeReport("RPT-001")
                        .medecin("Dr. Test")
                        .build()
                )
                .description("Test Element Result")
                .build();

        ElementResultEntity createdElementResult = elementResultService.createElementResult(elementResultEntity);

        // When
        createdElementResult.setResultatGeneral(false);
        createdElementResult.setDescription("Updated Test Element Result");
        Long id = createdElementResult.getId();
        createdElementResult.setId(null);
        ElementResultEntity updatedElementResult = elementResultService.updateElementResult(id ,createdElementResult);

        // Then
        assertEquals(id, updatedElementResult.getId());
        assertEquals(createdElementResult.isResultatGeneral(), updatedElementResult.isResultatGeneral());
        assertEquals(createdElementResult.getDescription(), updatedElementResult.getDescription());
    }
}
