package com.lapaix.report.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.lapaix.report.domain.entities.UniqueElementResultsEntity;
import com.lapaix.report.services.UniqueElementResultsService;

@WebMvcTest(UniqueElementResultsController.class)
public class UniqueElementResultsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniqueElementResultsService uniqueElementResultsService;

    @Test
    void testCount() throws Exception {
        // Mock the service behavior for count
        when(uniqueElementResultsService.count(any())).thenReturn(5L);

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/uniqueElementResults/count")
                        .param("search_string", "example")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("5"));

        // Verify that the service method was called
        verify(uniqueElementResultsService, times(1)).count("example");

        // Mock the service behavior for count when throwing an exception
        when(uniqueElementResultsService.count(eq("exception"))).thenThrow(new RuntimeException("Error"));

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/uniqueElementResults/count")
                        .param("search_string", "exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void testGetAllUniqueElementResults() throws Exception {
        // Create a list of unique element results entities
        List<UniqueElementResultsEntity> uniqueElementResultsEntities = Arrays.asList(
                UniqueElementResultsEntity.builder().id(1L).description("Test 1").build(),
                UniqueElementResultsEntity.builder().id(2L).description("Test 2").build()
        );
        
        // Mock the service behavior for getAllUniqueElementResults
        when(uniqueElementResultsService.getAllUniqueElementResults(any(), any())).thenReturn(new PageImpl<>(uniqueElementResultsEntities));

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/uniqueElementResults")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search_string", "example")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Test 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Test 2"));
        
        // Mock the service behavior for getAllUniqueElementResults when throwing an exception
        when(uniqueElementResultsService.getAllUniqueElementResults(eq("exception"), any())).thenThrow(new RuntimeException("Error"));

        // Perform the request and validate the response for exception case
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/uniqueElementResults")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search_string", "exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void testCreateUniqueElementResult() throws Exception {
        // Mock the service behavior
        when(uniqueElementResultsService.createUniqueElementResult(any())).thenReturn(UniqueElementResultsEntity.builder().id(1L).description("Test Description").build());

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/uniqueElementResults")
                        .param("description", "Test Description")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"));

        // Verify that the service method was called
        verify(uniqueElementResultsService, times(1)).createUniqueElementResult(any());
    }

    @Test
    void testUpdateUniqueElementResult() throws Exception {
        // Mock the service behavior
        when(uniqueElementResultsService.updateUniqueElementResult(eq(1L), any())).thenReturn(UniqueElementResultsEntity.builder().id(1L).description("Test Description").build());

        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/uniqueElementResults/1")
                        .param("description", "Test Description")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"));

        // Verify that the service method was called
        verify(uniqueElementResultsService, times(1)).updateUniqueElementResult(eq(1L), any());
    }

    @Test
    void testDeleteUniqueElementResult() throws Exception {
        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/uniqueElementResults/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify that the service method was called
        verify(uniqueElementResultsService, times(1)).deleteUniqueElementResult(eq(1L));
    }

    @Test
    void testHandleException() throws Exception {
        // Perform the request and validate the response
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/uniqueElementResults")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search_string", "exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }


    
}
