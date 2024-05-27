package com.lapaix.report.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import com.lapaix.report.domain.entities.ElementResultEntity;
import com.lapaix.report.domain.entities.ReportEntity;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReportServiceTest {

        @Autowired
        private ElementResultService elementResultService;

        @Autowired
        private ReportService reportService;

        @Test
        void testCount() {
                ReportEntity reportEntity1 = ReportEntity.builder()
                                .bulletinId(1L)
                                .date(LocalDateTime.now())
                                .titre("Test Report 1")
                                .medecin("Dr. Test 1")
                                .build();

                ReportEntity reportEntity2 = ReportEntity.builder()
                                .bulletinId(2L)
                                .date(LocalDateTime.now())
                                .titre("Test Report 2")
                                .medecin("Dr. Test 2")
                                .build();

                // When
                reportService.createReport(reportEntity1);
                reportService.createReport(reportEntity2);

                long totalCount = reportService.count("Test 1");

                assertEquals(1, totalCount);
        }

        @Test
        void testCreateReport() {

                ReportEntity reportEntity = ReportEntity.builder()
                                .bulletinId(1L)
                                .date(LocalDateTime.now())
                                .titre("Test Report")
                                .medecin("Dr. Test")
                                .build();

                // When
                ReportEntity createdReport = reportService.createReport(reportEntity);

                // Then
                assertNotNull(createdReport.getId());
                assertEquals(reportEntity.getBulletinId(), createdReport.getBulletinId());
                assertEquals(reportEntity.getDate(), createdReport.getDate());
                assertEquals(reportEntity.getTitre(), createdReport.getTitre());
                assertEquals(reportEntity.getMedecin(), createdReport.getMedecin());

        }

        @Test
        void testDeleteReport() {

                ReportEntity reportEntity = ReportEntity.builder()
                                .bulletinId(1L)
                                .date(LocalDateTime.now())
                                .titre("Test Report")
                                .medecin("Dr. Test")
                                .build();

                ReportEntity savedReport = reportService.createReport(reportEntity);
                Long reportId = savedReport.getId();

                // When
                reportService.deleteReport(reportId);

                // Then
                Optional<ReportEntity> optionalReport = reportService.getReportById(reportId);
                assertFalse(optionalReport.isPresent());

        }

        @Test
        void testGetAllReports() {
                ReportEntity reportEntity1 = ReportEntity.builder()
                                .bulletinId(1L)
                                .date(LocalDateTime.now())
                                .titre("Test Report 1")
                                .medecin("Dr. Test 1")
                                .build();

                ReportEntity reportEntity2 = ReportEntity.builder()
                                .bulletinId(2L)
                                .date(LocalDateTime.now())
                                .titre("Test Report 2")
                                .medecin("Dr. Test 2")
                                .build();

                reportService.createReport(reportEntity1);
                reportService.createReport(reportEntity2);

                // When
                Pageable pageable = PageRequest.of(0, 10);
                Page<ReportEntity> reportPage = reportService.getAllReports(null, pageable);

                // Then
                assertEquals(2, reportPage.getTotalElements());
                assertTrue(reportPage.stream()
                                .anyMatch(report -> report.getMedecin().equals("Dr. Test 1")));
                assertTrue(reportPage.stream()
                                .anyMatch(report -> report.getTitre().equals("Test Report 2")));

                // When
                pageable = PageRequest.of(0, 1);
                reportPage = reportService.getAllReports("", pageable);

                // Then
                assertEquals(2, reportPage.getTotalElements());
                assertEquals(1, reportPage.getNumberOfElements());

                // When
                pageable = PageRequest.of(0, 10);
                reportPage = reportService.getAllReports("Test Report 2", pageable);

                // Then
                assertEquals(1, reportPage.getTotalElements());
                assertTrue(reportPage.stream()
                                .allMatch(report -> report.getTitre().equals("Test Report 2")));

        }

        @Test
        void testGetReportById() {

                // Given
                ReportEntity reportEntity = ReportEntity.builder()
                                .bulletinId(1L)
                                .date(LocalDateTime.now())
                                .titre("Test Report")
                                .medecin("Dr. Test")
                                .build();

                ReportEntity savedReport = reportService.createReport(reportEntity);
                Long reportId = savedReport.getId();

                // When
                ReportEntity retrievedReport = reportService.getReportById(reportId).get();

                // Then
                assertNotNull(retrievedReport);
                assertEquals(reportEntity.getTitre(), retrievedReport.getTitre());
                assertEquals(reportEntity.getMedecin(), retrievedReport.getMedecin());
        }

        @Test
        void testGetReportElementResults() {

                // Given
                ReportEntity reportEntity = ReportEntity.builder()
                                .bulletinId(1L)
                                .date(LocalDateTime.now())
                                .titre("Test Report")
                                .medecin("Dr. Test")
                                .build();

                ReportEntity savedReport = reportService.createReport(reportEntity);
                Long reportId = savedReport.getId();

                ElementResultEntity elementResult1 = ElementResultEntity.builder()
                                .report(
                                                ReportEntity.builder()
                                                                .id(reportId)
                                                                .build()
                                )
                                .resultatGeneral(true)
                                .description("Test Element Result 1")
                                .createdDate(LocalDateTime.now())
                                .lastModifiedDate(LocalDateTime.now())
                                .build();

                ElementResultEntity elementResult2 = ElementResultEntity.builder()
                                .report(
                                                ReportEntity.builder()
                                                                .id(reportId)
                                                                .build())
                                .resultatGeneral(false)
                                .description("Test Element Result 2")
                                .createdDate(LocalDateTime.now())
                                .lastModifiedDate(LocalDateTime.now())
                                .build();

                elementResultService.createElementResult(elementResult1);
                elementResultService.createElementResult(elementResult2);

                // When
                List<ElementResultEntity> elementResults = reportService.getReportElementResults(reportId);

                // Then
                assertEquals(2, elementResults.size());
                assertTrue(elementResults.stream()
                                .anyMatch(result -> result.getDescription().equals("Test Element Result 1")));
                assertTrue(elementResults.stream()
                                .anyMatch(result -> result.getDescription().equals("Test Element Result 2")));

        }

        @Test
        void testUpdateReport() {

                // Given
                ReportEntity reportEntity = ReportEntity.builder()
                                .bulletinId(1L)
                                .date(LocalDateTime.now())
                                .titre("Test Report")
                                .medecin("Dr. Test")
                                .build();

                ReportEntity savedReport = reportService.createReport(reportEntity);
                Long reportId = savedReport.getId();

                ReportEntity updatedReport = ReportEntity.builder()
                                .id(reportId)
                                .bulletinId(2L)
                                .titre("Updated Report")
                                .medecin("Dr. Updated Test")
                                .build();

                // When
                updatedReport = reportService.updateReport(reportId, updatedReport);

                // Then
                Optional<ReportEntity> optionalReport = reportService.getReportById(reportId);
                assertTrue(optionalReport.isPresent());
                ReportEntity retrievedReport = optionalReport.get();
                assertEquals(updatedReport.getBulletinId(), retrievedReport.getBulletinId());
                assertEquals(updatedReport.getDate(), retrievedReport.getDate());
                assertEquals(updatedReport.getTitre(), retrievedReport.getTitre());
                assertEquals(updatedReport.getMedecin(), retrievedReport.getMedecin());

        }
}
