package com.lapaix.report.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lapaix.report.domain.dtos.UniqueElementResultsDTO;
import com.lapaix.report.domain.entities.UniqueElementResultsEntity;
import com.lapaix.report.services.UniqueElementResultsService;

@RestController
@RequestMapping("/api/uniqueElementResults")
@CrossOrigin(origins = "*")
public class UniqueElementResultsController {

    @Autowired
    private UniqueElementResultsService uniqueElementResultsService;

    @GetMapping("/count")
    public ResponseEntity<Long> count(
            @RequestParam(value = "search_string", required = true) String searchString) {

        long count = uniqueElementResultsService.count(searchString);
        return ResponseEntity.ok(count);
    }

    @GetMapping
    public ResponseEntity<List<UniqueElementResultsDTO>> getAllUniqueElementResults(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search_string", defaultValue = "") String searchString) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("description").descending());

        Page<UniqueElementResultsEntity> uniqueElementResultsPage = uniqueElementResultsService.getAllUniqueElementResults(searchString,
                pageable);
        List<UniqueElementResultsDTO> uniqueElementResultsDTOs = UniqueElementResultsDTO.toDTOs(uniqueElementResultsPage.getContent());
        return ResponseEntity.ok(uniqueElementResultsDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniqueElementResultsDTO> getUniqueElementResultById(@PathVariable Long id) {
        Optional<UniqueElementResultsEntity> uniqueElementResult = uniqueElementResultsService.getUniqueElementResultById(id);
        return uniqueElementResult.map(entity -> ResponseEntity.ok(UniqueElementResultsDTO.toDTO(entity)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UniqueElementResultsDTO> createUniqueElementResult(@RequestParam(value = "description", required = true) String description) {
        UniqueElementResultsEntity uniqueElementResult = new UniqueElementResultsEntity();
        uniqueElementResult.setDescription(description);
        uniqueElementResult = uniqueElementResultsService.createUniqueElementResult(uniqueElementResult);
        return ResponseEntity.ok(UniqueElementResultsDTO.toDTO(uniqueElementResult));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniqueElementResultsDTO> updateUniqueElementResult(@PathVariable Long id, @RequestParam(value = "description", required = true) String description) {
        UniqueElementResultsEntity uniqueElementResult = new UniqueElementResultsEntity();
        uniqueElementResult.setDescription(description);
        uniqueElementResult = uniqueElementResultsService.updateUniqueElementResult(id, uniqueElementResult);
        return ResponseEntity.ok(UniqueElementResultsDTO.toDTO(uniqueElementResult));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniqueElementResult(@PathVariable Long id) {
        uniqueElementResultsService.deleteUniqueElementResult(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
    
}
