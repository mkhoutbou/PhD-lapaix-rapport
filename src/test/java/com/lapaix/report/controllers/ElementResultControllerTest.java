package com.lapaix.report.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lapaix.report.domain.dtos.ElementResultDTO;
import com.lapaix.report.domain.entities.ElementResultEntity;
import com.lapaix.report.services.ElementResultService;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(ElementResultController.class)
public class ElementResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ElementResultService elementResultService;

    @Test
    void testCount() throws Exception {
        // Mock the service behavior for count
        when(elementResultService.count(any())).thenReturn(5L);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/elementResults/count")
                        .param("search_string", "example")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("5"));

        // Verify that the service method was called
        verify(elementResultService, times(1)).count("example");

        // Mock the service behavior for count when throwing an exception
        when(elementResultService.count(eq("exception"))).thenThrow(new RuntimeException("Error"));

        // Perform the request and validate the response for exception case
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/elementResults/count")
                        .param("search_string", "exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void testCreateElementResult() throws Exception {
        // Create a test elementResultDTO
        ElementResultDTO testElementResultDTO = new ElementResultDTO();
        testElementResultDTO.setId(1L);
        testElementResultDTO.setDescription("Test Description");

        // Mock the service behavior
        when(elementResultService.createElementResult(any())).thenReturn(testElementResultDTO.toEntity());

        // Convert the elementResultDTO to JSON
        String elementResultJson = objectMapper.writeValueAsString(testElementResultDTO);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/elementResults")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(elementResultJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testElementResultDTO.getId()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.description").value(testElementResultDTO.getDescription()));

        // Verify that the service method was called
        verify(elementResultService, times(1)).createElementResult(any());
    }

    @Test
    void testDeleteElementResult() {

        // Specify the elementResult ID to be deleted
        Long elementResultIdToDelete = 1L;

        // Mock the service behavior
        doNothing().when(elementResultService).deleteElementResult(elementResultIdToDelete);

        // Perform the request and validate the response
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.delete("/api/elementResults/{id}", elementResultIdToDelete))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify that the service method was called
        verify(elementResultService, times(1)).deleteElementResult(elementResultIdToDelete);
    }

    @Test
    void testGetAllElementResults() throws Exception {
        // Create test element results
        ElementResultEntity elementResult1 = new ElementResultEntity();
        elementResult1.setId(1L);
        elementResult1.setDescription("Result 1");

        ElementResultEntity elementResult2 = new ElementResultEntity();
        elementResult2.setId(2L);
        elementResult2.setDescription("Result 2");

        List<ElementResultEntity> elementResults = Arrays.asList(elementResult1, elementResult2);

        // Create a Page of element results
        Page<ElementResultEntity> elementResultsPage = new PageImpl<>(elementResults, PageRequest.of(0, 10),
                elementResults.size());

        // Mock the service behavior
        when(elementResultService.getAllElementResults(anyString(), any())).thenReturn(elementResultsPage);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/elementResults")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search_string", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(elementResults.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description")
                        .value(elementResults.get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(elementResults.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description")
                        .value(elementResults.get(1).getDescription()));
    }

    @Test
    void testGetElementResultById() throws Exception {
        // Create a test element result entity
        ElementResultEntity testElementResult = new ElementResultEntity();
        testElementResult.setId(1L);
        testElementResult.setDescription("Test Description");

        // Mock the service behavior
        when(elementResultService.getElementResultById(testElementResult.getId()))
                .thenReturn(Optional.of(testElementResult));

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/elementResults/{id}", testElementResult.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testElementResult.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(testElementResult.getDescription()));
    }

    @Test
    void testUpdateElementResult() throws Exception {
        // Create a test elementResultDTO
        ElementResultDTO updatedElementResultDTO = new ElementResultDTO();
        updatedElementResultDTO.setId(1L);
        updatedElementResultDTO.setDescription("Updated Description");

        // Mock the service behavior
        when(elementResultService.updateElementResult(eq(updatedElementResultDTO.getId()), any()))
                .thenReturn(updatedElementResultDTO.toEntity());

        // Convert the updated elementResultDTO to JSON
        String updatedElementResultJson = objectMapper.writeValueAsString(updatedElementResultDTO);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/elementResults/{id}", updatedElementResultDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedElementResultJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedElementResultDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value(updatedElementResultDTO.getDescription()));
    }

}
