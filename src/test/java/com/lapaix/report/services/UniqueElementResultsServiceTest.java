package com.lapaix.report.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import com.lapaix.report.domain.entities.UniqueElementResultsEntity;
import com.lapaix.report.repositories.UniqueElementResultsRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UniqueElementResultsServiceTest {

    @Autowired
    private UniqueElementResultsService uniqueElementResultsService;

    @Autowired
    private UniqueElementResultsRepository uniqueElementResultsRepository;

    @Test
    void testCount() {

        // Given
        UniqueElementResultsEntity uniqueElementResultsEntity1 = UniqueElementResultsEntity.builder()
                .resultatGeneral(true)
                .description("Test Unique Element Result 1")
                .build();
        
        UniqueElementResultsEntity uniqueElementResultsEntity2 = UniqueElementResultsEntity.builder()
                .resultatGeneral(false)
                .description("Test Unique Element Result 2")
                .build();

        UniqueElementResultsEntity uniqueElementResultsEntity3 = UniqueElementResultsEntity.builder()
                .resultatGeneral(true)
                .description("Test Unique Element Result 3")
                .build();

        // When
        uniqueElementResultsRepository.save(uniqueElementResultsEntity1);
        uniqueElementResultsRepository.save(uniqueElementResultsEntity2);
        uniqueElementResultsRepository.save(uniqueElementResultsEntity3);

        // Then
        long count = uniqueElementResultsService.count("");
        assertEquals(3, count);

        count = uniqueElementResultsService.count("Test Unique Element Result 1");
        assertEquals(1, count);

        count = uniqueElementResultsService.count("Test Unique Element Result 2");
        assertEquals(1, count);

        count = uniqueElementResultsService.count("Test Unique Element Result 3");
        assertEquals(1, count);

        count = uniqueElementResultsService.count("Test Unique Element Result 4");
        assertEquals(0, count);

        
    }

    @Test
    void testCreateUniqueElementResult() {

        // Given
        UniqueElementResultsEntity uniqueElementResultsEntity = UniqueElementResultsEntity.builder()
                .resultatGeneral(true)
                .description("Test Unique Element Result")
                .build();

        // When
        uniqueElementResultsEntity = uniqueElementResultsService.createUniqueElementResult(uniqueElementResultsEntity);

        // Then
        assertEquals(1, uniqueElementResultsRepository.count());
        assertEquals("Test Unique Element Result", uniqueElementResultsEntity.getDescription());
    }

    @Test
    void testUpdateUniqueElementResult() {

        // Given
        UniqueElementResultsEntity uniqueElementResultsEntity = UniqueElementResultsEntity.builder()
                .resultatGeneral(true)
                .description("Test Unique Element Result")
                .build();

        uniqueElementResultsEntity = uniqueElementResultsRepository.save(uniqueElementResultsEntity);

        // When
        uniqueElementResultsEntity.setDescription("Updated Unique Element Result");
        uniqueElementResultsEntity = uniqueElementResultsService.updateUniqueElementResult(uniqueElementResultsEntity.getId(), uniqueElementResultsEntity);

        // Then
        assertEquals(1, uniqueElementResultsRepository.count());
        assertEquals("Updated Unique Element Result", uniqueElementResultsEntity.getDescription());
    }

    @Test
    void testDeleteUniqueElementResult() {

        // Given
        UniqueElementResultsEntity uniqueElementResultsEntity = UniqueElementResultsEntity.builder()
                .resultatGeneral(true)
                .description("Test Unique Element Result")
                .build();

        uniqueElementResultsEntity = uniqueElementResultsRepository.save(uniqueElementResultsEntity);

        // When
        uniqueElementResultsService.deleteUniqueElementResult(uniqueElementResultsEntity.getId());

        // Then
        assertEquals(0, uniqueElementResultsRepository.count());

    }

    @Test
    void testGetAllUniqueElementResults() {

        // Given
        UniqueElementResultsEntity uniqueElementResultsEntity1 = UniqueElementResultsEntity.builder()
                .resultatGeneral(true)
                .description("Test Unique Element Result 1")
                .build();
        UniqueElementResultsEntity uniqueElementResultsEntity2 = UniqueElementResultsEntity.builder()
                .resultatGeneral(false)
                .description("Test Unique Element Result 2")
                .build();
        
        // When
        uniqueElementResultsRepository.save(uniqueElementResultsEntity1);
        uniqueElementResultsRepository.save(uniqueElementResultsEntity2);

        // Then
        Pageable pageable = PageRequest.of(0, 10);
        assertEquals(2, uniqueElementResultsService.getAllUniqueElementResults(null, pageable).get().count());
        assertEquals(1, uniqueElementResultsService.getAllUniqueElementResults("Result 2", pageable).get().count());
    }

    @Test
    void testGetUniqueElementResultById() {

        // Given
        UniqueElementResultsEntity uniqueElementResultsEntity = UniqueElementResultsEntity.builder()
                .resultatGeneral(true)
                .description("Test Unique Element Result")
                .build();

        UniqueElementResultsEntity createdUniqueElementResult = uniqueElementResultsService.createUniqueElementResult(uniqueElementResultsEntity);

        // When
        UniqueElementResultsEntity foundUniqueElementResult = uniqueElementResultsService.getUniqueElementResultById(createdUniqueElementResult.getId()).get();

        // Then
        assertEquals(createdUniqueElementResult.getId(), foundUniqueElementResult.getId());

    }
    
}
