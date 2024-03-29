package com.lapaix.report.controllers;

import com.lapaix.report.domain.dtos.ElementResultDTO;
import com.lapaix.report.domain.entities.ElementResultEntity;
import com.lapaix.report.services.ElementResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/elementResults")
@CrossOrigin(origins = "*")
public class ElementResultController {

    @Autowired
    private ElementResultService elementResultService;

    @GetMapping("/count")
    public ResponseEntity<Long> count(
            @RequestParam(value = "search_string", required = true) String searchString) {

        long count = elementResultService.count(searchString);
        return ResponseEntity.ok(count);
    }

    @GetMapping
    public ResponseEntity<List<ElementResultDTO>> getAllElementResults(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search_string", defaultValue = "") String searchString) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("description").descending());

        Page<ElementResultEntity> elementResultsPage = elementResultService.getAllElementResults(searchString,
                pageable);
        List<ElementResultDTO> elementResultDTOs = ElementResultDTO.toDTOs(elementResultsPage.getContent());
        return ResponseEntity.ok(elementResultDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElementResultDTO> getElementResultById(@PathVariable Long id) {
        Optional<ElementResultEntity> elementResult = elementResultService.getElementResultById(id);
        return elementResult.map(entity -> ResponseEntity.ok(ElementResultDTO.toDTO(entity)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ElementResultDTO> createElementResult(@RequestBody ElementResultDTO elementResultDTO) {
        ElementResultEntity elementResult = elementResultService.createElementResult(elementResultDTO.toEntity());
        return ResponseEntity.ok(ElementResultDTO.toDTO(elementResult));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ElementResultDTO> updateElementResult(
            @PathVariable Long id, @RequestBody ElementResultDTO elementResultDTO) {
        ElementResultDTO updatedElementResultDTO = ElementResultDTO.toDTO(
                elementResultService.updateElementResult(id, elementResultDTO.toEntity()));
        return ResponseEntity.ok(updatedElementResultDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteElementResult(@PathVariable Long id) {
        elementResultService.deleteElementResult(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }

}
