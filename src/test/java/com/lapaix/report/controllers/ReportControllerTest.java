package com.lapaix.report.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lapaix.report.domain.dtos.ReportDTO;
import com.lapaix.report.domain.entities.ElementResultEntity;
import com.lapaix.report.domain.entities.ReportEntity;
import com.lapaix.report.services.ReportService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportService reportService;

    @Test
    void testCount() throws Exception {
        // Mock the service behavior for count
        when(reportService.count(any())).thenReturn(5L);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/reports/count")
                        .param("search_string", "example")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("5"));

        // Verify that the service method was called
        verify(reportService, times(1)).count("example");

        // Mock the service behavior for count when throwing an exception
        when(reportService.count(eq("exception"))).thenThrow(new RuntimeException("Error"));

        // Perform the request and validate the response for exception case
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/reports/count")
                        .param("search_string", "exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void testCreateReport() throws Exception {
        // Create a test reportDTO
        ReportDTO testReportDTO = new ReportDTO();
        testReportDTO.setId(1L);
        testReportDTO.setBulletinId(1L);
        testReportDTO.setCodeReport("CODE001");
        testReportDTO.setDate(LocalDateTime.now());
        testReportDTO.setTitre("Test Report");
        testReportDTO.setMedecin("Dr. Smith");
        testReportDTO.setConclusion("Test conclusion");

        // Mock the service behavior
        when(reportService.createReport(any())).thenReturn(testReportDTO.toEntity());

        // Convert the reportDTO to JSON
        String reportJson = objectMapper.writeValueAsString(testReportDTO);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reportJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testReportDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bulletinId").value(testReportDTO.getBulletinId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.codeReport").value(testReportDTO.getCodeReport()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titre").value(testReportDTO.getTitre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medecin").value(testReportDTO.getMedecin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.conclusion").value(testReportDTO.getConclusion()));

        // Verify that the service method was called
        verify(reportService, times(1)).createReport(any());
    }

    @Test
    void testDeleteReport() {

        // Specify the report ID to be deleted
        Long reportIdToDelete = 1L;

        // Mock the service behavior
        doNothing().when(reportService).deleteReport(reportIdToDelete);

        // Perform the request and validate the response
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.delete("/api/reports/{id}", reportIdToDelete))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify that the service method was called
        verify(reportService, times(1)).deleteReport(reportIdToDelete);
    }

    @Test
    void testGetAllReports() throws Exception {
        // Create test reports
        ReportEntity report1 = new ReportEntity();
        report1.setId(1L);
        report1.setTitre("Report 1");

        ReportEntity report2 = new ReportEntity();
        report2.setId(2L);
        report2.setTitre("Report 2");

        List<ReportEntity> reports = Arrays.asList(report1, report2);

        // Create a Page of reports
        Page<ReportEntity> reportsPage = new PageImpl<>(reports, PageRequest.of(0, 10), reports.size());

        // Mock the service behavior
        when(reportService.getAllReports(anyString(), any())).thenReturn(reportsPage);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/reports")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search_string", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(reports.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].titre").value(reports.get(0).getTitre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(reports.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].titre").value(reports.get(1).getTitre()));
    }

    @Test
    void testGetReportById() throws Exception {
        // Create a test report entity
        ReportEntity testReport = new ReportEntity();
        testReport.setId(1L);
        testReport.setTitre("Test Report");
        testReport.setMedecin("Dr. Smith");

        // Mock the service behavior
        when(reportService.getReportById(testReport.getId())).thenReturn(Optional.of(testReport));

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/reports/{id}", testReport.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testReport.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titre").value(testReport.getTitre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medecin").value(testReport.getMedecin()));
    }

    @Test
    void testGetReportElementResults() throws Exception {
        // Create a test report entity
        ReportEntity testReport = new ReportEntity();
        testReport.setId(1L);

        // Create test element result entities
        ElementResultEntity elementResult1 = new ElementResultEntity();
        elementResult1.setId(1L);
        elementResult1.setDescription("Result 1");

        ElementResultEntity elementResult2 = new ElementResultEntity();
        elementResult2.setId(2L);
        elementResult2.setDescription("Result 2");

        testReport.setResults(Arrays.asList(elementResult1, elementResult2));

        // Mock the service behavior
        when(reportService.getReportById(testReport.getId())).thenReturn(Optional.of(testReport));

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/reports/{reportId}/elementResults", testReport.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(elementResult1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(elementResult1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(elementResult2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(elementResult2.getDescription()));
    }

    @Test
    void testUpdateReport() throws Exception {
        // Create a test reportDTO
        ReportDTO updatedReportDTO = new ReportDTO();
        updatedReportDTO.setId(1L);
        updatedReportDTO.setCodeReport("UpdatedCode");
        updatedReportDTO.setTitre("UpdatedTitle");

        // Mock the service behavior
        when(reportService.updateReport(eq(updatedReportDTO.getId()), any())).thenReturn(updatedReportDTO.toEntity());

        // Convert the updated reportDTO to JSON
        String updatedReportJson = objectMapper.writeValueAsString(updatedReportDTO);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/reports/{id}", updatedReportDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedReportJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedReportDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.codeReport").value(updatedReportDTO.getCodeReport()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titre").value(updatedReportDTO.getTitre()));
    }

}
