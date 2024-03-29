package com.lapaix.report.domain.dtos;

import org.modelmapper.ModelMapper;
import com.lapaix.report.domain.entities.ElementResultEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElementResultDTO {
    private Long id;
    private ReportDTO report;
    private boolean resultatGeneral;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    // Getters and Setters

    public static ElementResultDTO toDTO(ElementResultEntity elementResult) {
        if(elementResult == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(elementResult, ElementResultDTO.class);
    }

    public ElementResultEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ElementResultEntity.class);
    }

    public static List<ElementResultEntity> toEntities(List<ElementResultDTO> dtos) {
        if (dtos == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return dtos.stream()
                .map(dto -> dto == null ? null : modelMapper.map(dto, ElementResultEntity.class))
                .collect(Collectors.toList());
    }

    public static List<ElementResultDTO> toDTOs(List<ElementResultEntity> elementResults) {
        if (elementResults == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return elementResults.stream()
                .map(entity -> entity == null ? null : modelMapper.map(entity, ElementResultDTO.class))
                .collect(Collectors.toList());
    }
}
